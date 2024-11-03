package client.network;

import client.customexceptions.ServerCommunicationException;
import client.customexceptions.UserInputException;
import client.model.gamemap.GameMap;
import client.network.servercompatlayer.ClientToNetworkDataConverter;
import client.network.servercompatlayer.ModelDataEnvelope;
import client.network.servercompatlayer.NetworkToClientDataConverter;
import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.URL;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;



/**
 * This class was largely "recycled" from my SS24 submission!
 */
public class ClientNetwork {

/**
 *  Attributes:
 */
	
	private WebClient baseWebClient;

	private UniqueGameIdentifier currentGameID;

	private UniquePlayerIdentifier myPlayerID;

	private GameState cachedGameState;

	private NetworkToClientDataConverter fromServer;

	private ClientToNetworkDataConverter toServer;

	
	private Logger logger;

/**
 * Methods:	
 */

	/**
	 * @param serverBaseUrl 
	 * @param currentGameID
	 * @throws UserInputException if no UniqueGameIdentifier is provided 
	 */
	public ClientNetwork(URL serverBaseUrl, UniqueGameIdentifier currentGameID) throws UserInputException {

		this.logger = LoggerFactory.getLogger(ClientNetwork.class);

		// From example main method	
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // we send XML (cf. network protocol)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE) // we receive XML (cf. network protocol)
				.build();

		if (currentGameID != null) {
			this.currentGameID = currentGameID;
		} else {
			throw new UserInputException("Cannot start a game without a game UniqueGameIdentifier!");
		}

		this.myPlayerID = requestPlayerID();

	}

	/**
	 * @return 
	 * @throws UserInputException 
	 * 
	 */
	private UniquePlayerIdentifier requestPlayerID() throws UserInputException {
		PlayerRegistration playerReg = new PlayerRegistration(
				"Ivan",
				"Bratanov",
				"bratanovi02");
		Mono<ResponseEnvelope> webAccess = baseWebClient
				.method(HttpMethod.POST)
				.uri("/" + currentGameID + "/players")
				.body(BodyInserters.fromValue(playerReg)) // specify the data which is sent to the server
				.retrieve().bodyToMono(ResponseEnvelope.class); 
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			logger.error("requestPlayerId failed, errormessage: " + resultReg.getExceptionMessage());
			throw new UserInputException(resultReg.getExceptionMessage()
					+ ". Please make sure you've created a new game before executing the client!"
					+ " Please make sure you enter the relevant gameId when running the client!");
		} else {
			UniquePlayerIdentifier uniqueID = resultReg.getData().get();
			logger.info("My Player ID: " + uniqueID.getUniquePlayerID());
			return uniqueID;
		}
	}

	/**
	 * @param localHalfMap
	 */
	public void sendLocalMapToServer(GameMap localHalfMap) {
		
		delayRequest();
		
		PlayerHalfMap playerHalfMap = toServer.getPlayerHalfMap(myPlayerID, localHalfMap);

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + myPlayerID + "/halfmaps")
				.body(BodyInserters.fromValue(playerHalfMap)) // specify the data which is sent to the server
				.retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		ResponseEnvelope<UniquePlayerIdentifier> resMapPost = webAccess.block();

		if (resMapPost.getState() == ERequestState.Error) {

			logger.error("sendHalfMap failed, errormessage: " + resMapPost.getExceptionMessage());
		} else {
			logger.info("Client map half was successfully sent to the server");
		}
	}


	/**
	 * Used for polling the server
	 */
	private void updateCachedGameState() {
		
		logger.debug("retrieving the most recent GameState from the Server");
		delayRequest();
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + currentGameID + "/states/" + myPlayerID).retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope<GameState> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			logger.error("requestGameState failed, errormessage: " + requestResult.getExceptionMessage());

		} else if (requestResult.getData() == null) {
			logger.error("requestGameState received no data!");

		} else if (requestResult.getData().get().getGameStateId() == cachedGameState.getGameStateId()) {
			logger.warn("Attempting to update the cached GameState returned the old GameState. Client is sending requests too rapidly!");
			delayRequest();
			updateCachedGameState(); // Make sure does not cause infinite loop
		} else {
			logger.debug("Received valid GameState!");
			cachedGameState = requestResult.getData().get();
		}
	}

	/**
	 * @return
	 */
	private boolean askIfMyTurn() {

		updateCachedGameState();
		PlayerState playerState = getMyPlayerState();
		
		 if (playerState.getState()  == EPlayerGameState.MustAct) {
			return true;
		} else {
			logger.warn("askIfMyTurn determined playerState = " + playerState);
			return false;
		}
	}

	private PlayerState getMyPlayerState() {

		Set<PlayerState> players = cachedGameState.getPlayers();

		for (PlayerState playerState : players) {
			if (playerState.getUniquePlayerID().equals(myPlayerID)) {
				return playerState;
			}
		}
		throw new ServerCommunicationException("Couldn't retrieve my PlayerState from the cached GameState!");
	}
	

	/**
	 * @return
	 */
	public ModelDataEnvelope getModelData() {
		
		updateCachedGameState();
		PlayerState myCurrentPlayerState = getMyPlayerState();

		ModelDataEnvelope newModelDataEnvelope = fromServer.getModelDataEnvelope(cachedGameState.getMap(), 
																				 myCurrentPlayerState.hasCollectedTreasure(),
																				 myCurrentPlayerState.getState());
		return newModelDataEnvelope;
	}

	/**
	 * @param moveDir
	 */
	public void sendMove(EMove moveDir) {
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + currentGameID + "/moves")
				.body(BodyInserters.fromValue(moveDir)) 
				.retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope<UniquePlayerIdentifier> resMovePost = webAccess.block();
		
		if(resMovePost.getState() == ERequestState.Error) {
			throw new ServerCommunicationException("Could not send EMove to Server!");
		}
	}

	/**
	 * 
	 */
	public void busyWaitForMyTurn() {

		boolean isMyTurn = false;

		while (!isMyTurn) {

			isMyTurn = askIfMyTurn();

		}
	}

	/**
	 * 
	 */
	private void delayRequest() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}