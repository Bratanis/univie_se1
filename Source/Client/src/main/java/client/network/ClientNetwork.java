package client.network;

import client.customexceptions.ServerCommunicationException;
import client.customexceptions.UserInputException;
import client.model.gamemap.GameMap;
import client.network.servercompatlayer.ClientToServerDataConverter;
import client.network.servercompatlayer.ModelDataEnvelope;
import client.network.servercompatlayer.ServerToClientDataConverter;
import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.FullMap;
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
	private boolean registeredToAGame;
	
	private WebClient baseWebClient;

	private UniqueGameIdentifier currentGameID;

	private UniquePlayerIdentifier myPlayerID;

	private GameState cachedGameState;

	private ServerToClientDataConverter fromServer;

	private ClientToServerDataConverter toServer;

	
	private Logger logger;

/**
 * Methods:	
 */

	/**
	 * @param serverBaseUrl 
	 * @param currentGameID
	 * @throws UserInputException if no UniqueGameIdentifier is provided 
	 */
	public ClientNetwork(URL serverBaseUrl, UniqueGameIdentifier currentGameID) {

		this.logger = LoggerFactory.getLogger(ClientNetwork.class);
		this.toServer = new ClientToServerDataConverter();
		this.fromServer = new ServerToClientDataConverter();

		// From example main method	
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) 
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE) 
				.build();

		if (currentGameID != null) {
			this.currentGameID = currentGameID;
		} else {
			logger.error("User is likely trying to start a game without a GameID!"); // Ctor should not throw an exception, 
																//but one will occur at "reuestPlayerID()" if the gameID is invalid
			this.currentGameID = UniqueGameIdentifier.of("");
		}


	}
	
	public boolean isRegistered() {
		return this.registeredToAGame;
	}

	
	public void registerClient() throws UserInputException {
		this.myPlayerID = requestPlayerID();
		if (myPlayerID == null)
			throw new RuntimeException ("Client registration failed: could not retrieve the playerID from server");
		this.registeredToAGame=true;
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
				.uri("/" + currentGameID.getUniqueGameID() + "/players")
				.body(BodyInserters.fromValue(playerReg)) 
				.retrieve().bodyToMono(ResponseEnvelope.class); 
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			throw new UserInputException(resultReg.getExceptionMessage()
					+ ". Please make sure you've created a new game before executing the client!"
					+ " Please make sure you enter the relevant gameId when running the client!");
		} else {
			UniquePlayerIdentifier uniqueID = resultReg.getData().get();
			//logger.info("My Player ID: " + uniqueID.getUniquePlayerID());
			return uniqueID;
		}
	}

	/**
	 * @param localHalfMap
	 */
	public void sendLocalMapToServer(GameMap localHalfMap) {
		
		delayRequest();
		
		PlayerHalfMap playerHalfMap = toServer.getPlayerHalfMap(myPlayerID, localHalfMap);
		
		assert (playerHalfMap != null);
		
		Mono<ResponseEnvelope> webAccess = baseWebClient
				.method(HttpMethod.POST)
				.uri("/" + currentGameID.getUniqueGameID()  + "/halfmaps")
				.body(BodyInserters.fromValue(playerHalfMap))
				.retrieve().bodyToMono(ResponseEnvelope.class);

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
		
//		logger.debug("retrieving the most recent GameState from the Server");
		delayRequest();
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + currentGameID.getUniqueGameID() + "/states/" + myPlayerID.getUniquePlayerID()).retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope<GameState> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			logger.error("requestGameState failed, errormessage: " + requestResult.getExceptionMessage());

		} else if (requestResult.getData() == null) {
			logger.error("requestGameState received no data!");

		} else if ((cachedGameState != null) && (requestResult.getData().get().getGameStateId().equals(cachedGameState.getGameStateId()))) {
			//logger.warn("Attempting to update the cached GameState returned the old GameState. Client is sending requests too rapidly!");
//			delayRequest();
//			updateCachedGameState();
		} else {
			//logger.debug("Received valid GameState!");
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
			if (playerState.getState() == EPlayerGameState.Lost) {
				logger.error("player Lost! (due to a broken rule!)");
				System.exit(0);
			}
//			logger.debug("askIfMyTurn determined playerState = " + playerState);
			return false;
		}
	}

	private PlayerState getMyPlayerState() {

		updateCachedGameState();
		Set<PlayerState> players = cachedGameState.getPlayers();

		for (PlayerState playerState : players) {
			if (playerState.getUniquePlayerID().equals(myPlayerID.getUniquePlayerID())) {
				return playerState;
			}
		}
		throw new ServerCommunicationException("Couldn't retrieve my PlayerState from the cached GameState(= " + cachedGameState + ")");
	}
	

	/**
	 * @return
	 */
	public ModelDataEnvelope getModelData() {
		
		FullMap serverMap = new FullMap();

		while (serverMap.isEmpty() || serverMap.getMapNodes().size() <= 50) { // Wait until the server sends the full map
			updateCachedGameState();
			serverMap = cachedGameState.getMap();
		}
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
		
		PlayerMove moveToBeSent = PlayerMove.of(myPlayerID, moveDir);
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + currentGameID.getUniqueGameID() + "/moves")
				.body(BodyInserters.fromValue(moveToBeSent)) 
				.retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope<UniquePlayerIdentifier> resMovePost = webAccess.block();
		
		if(resMovePost.getState() == ERequestState.Error) {
			throw new ServerCommunicationException("Could not send EMove to Server: " + resMovePost.getExceptionMessage());
		}
	}

	/**
	 * 
	 */
	public void busyWaitForMyTurn() {
//		if (getMyPlayerState().getState() == EPlayerGameState.Lost)
//			throw new RuntimeException("Client unexpectedly lost (due to a broken rule)!");
		boolean isMyTurn = false;

		while (!isMyTurn ) {
//			logger.debug("Still waiting for my turn...");
			isMyTurn = askIfMyTurn();

		}
	}

	/**
	 * 
	 */
	private void delayRequest() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}