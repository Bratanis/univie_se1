package client.main;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import client.customexceptions.UserInputException;
import client.gamelogic.manager.GameManager;
import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.controller.MvcController;
import client.mvc.model.GameModel;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.view.CLIView;
import client.mvc.view.EmojiCliView;
import client.network.ClientNetwork;
import messagesbase.UniqueGameIdentifier;

public class MainClient {

	public static void main(String[] args)  {
		
       // TR http://swe1.wst.univie.ac.at:18235 GameID 
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
			CLIView theView = new EmojiCliView();
			
			// Create the notification collector here to enable dependency injection where it is needed
			MvcNotificationCollector mvcNotificationCollector = new MvcNotificationCollector();
			
			MvcController mvcCtl= new MvcController (theModel, theView, mvcNotificationCollector); // Dependency injection for modularity 
	
			GameManager theController = new GameManager(theNetwork, mvcCtl); // Dependency injection for modularity 
			theController.initializeGame(); // Register the client
			theController.initialMapExchange(); // Send the locally generated half map and get server full map
			theController.startGame(); // Start sending moves and updating the local data until game is finished
			
		} catch (UserInputException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
}
