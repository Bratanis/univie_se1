package client.main;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import client.controller.GameController;
import client.network.ClientNetwork;
import client.customexceptions.UserInputException;
import client.model.GameModel;
import client.view.CLIView;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromserver.GameState;
import reactor.core.publisher.Mono;

public class MainClient {

	public static void main(String[] args) throws UserInputException {

		try {
			//String gameMode = args[0]; Not used anywhere in my implementation
	
			URL serverBaseUrl;
			try {
				serverBaseUrl = (new URI(args[1])).toURL();
			} catch (MalformedURLException | URISyntaxException e) {
				throw new UserInputException("Invalid URL. Could not connect to Server;");
			}
	
			UniqueGameIdentifier currentGameID = UniqueGameIdentifier.of(args[2]);
		
			
			ClientNetwork theNetwork = new ClientNetwork(serverBaseUrl, currentGameID);	
			GameModel theModel = new GameModel();
			CLIView theView = new CLIView();
	
			GameController theController = new GameController(theNetwork, theModel, theView); // Dependency injection for modularity 
			theController.initializeGame(); // Register the client
			theController.initialMapExchange(); // Send the locally generated half map and get server full map
			//theController.startGame(); // Start sending moves and updating the local data until game is finished
			
		} catch (UserInputException e) {
			e.printStackTrace();
		}
	}
}
