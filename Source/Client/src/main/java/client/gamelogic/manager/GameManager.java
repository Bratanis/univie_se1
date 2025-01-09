package client.gamelogic.manager;

import client.customexceptions.UserInputException;
import client.gamelogic.mapcreation.MapGenerator;
import client.gamelogic.mapcreation.validation.MapValidator;
import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.gamelogic.pathfinder.PathFinder;
import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.mvc.controller.MvcController;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.network.ClientNetwork;
import client.network.servercompatlayer.ServerDataEnvelope;
import messagesbase.messagesfromclient.EMove;

/**
 * Class containing all of the game logic, coordinating the execution according to the business rules 
 */
public class GameManager {

	private ClientNetwork theNetwork;
	private MvcController mvcController;
	private PathFinder pathFinder;
	
	private boolean gameOver;
//	private GameModel theModel;
//	private CLIView theView;

	/**
	 * @param network 
	 * @param model 
	 * @param view
	 */
	public GameManager(ClientNetwork network, MvcController mvcController) {
		this.theNetwork = network;
		this.mvcController = mvcController;
		this.pathFinder = PathFinder.getUndifinedInstance();
	}


	public void initializeGame() throws UserInputException{
		if(!theNetwork.isRegistered()) {
			theNetwork.registerClient();
		}
	}
	

	/**
	 * 
	 */
	public void initialMapExchange() {
		
		// set the initially generated map in the mvc
		ClientHalfMap clientHalfMap = generateValidHalfMap();
		mvcController.setInitialHalfMap(clientHalfMap); 
	
		// exchange maps via the server
		theNetwork.busyWaitForMyTurn();
		theNetwork.sendLocalMapToServer(clientHalfMap);
		ServerDataEnvelope newData = theNetwork.getServerData();
		
		// create the pathFinder and feed it relevand data (what do we do with the pf in the ctor?)
		initializePathFinder(newData);
		
		//
		mvcController.updateModel(newData.getNewModel());
		
	}
	
	/**
	 * replaces the undefined path finder with a defined one that can be used to calculate the moves sent to the server
	 */
	private void initializePathFinder(ServerDataEnvelope data) {
		MapTerritories territories = data.getTerritories();
		PathFinderData initialPFData = data.getPfData();
		this.pathFinder = new PathFinder(initialPFData, territories);
	}
	
	private ClientHalfMap generateValidHalfMap() {
		MapGenerator generator = new MapGenerator();
		
		NotificationCollector mvcNotificationCollector = mvcController.getTechnicalInternalsModel();
		MapValidator validator = new MapValidator(mvcNotificationCollector); // Dependency injection to link the mvc with the business logic
		
		ClientHalfMap testHalfMap = generator.offerHalfMap();

		//Place an invalid map initially to showcase the map validation internals
//		ClientHalfMap testHalfMap = new ClientHalfMap(ClientHalfMap.getGrassOnlyMapFields());
		
		while (!validator.mapIsValid(testHalfMap)) {
//			logger.info("Generated an invalid map. Retrying...");
			testHalfMap = generator.offerHalfMap();
		}

		return testHalfMap;
	}

	/**
	 * Keep sending the next move determined by the pathfinder (inside the model) and updating the data 
	 * with the server response until the game is over 
	 */
	public void startGame() {
		while (!gameOver) {
//			theNetwork.busyWaitForMyTurn();
//			EMove nextMove = theModel.getNextMove();
//			//System.out.println("MY NEXT MOVE: " + nextMove); // FOR DEBUGGING
//			theNetwork.sendMove(nextMove);
//			ServerDataEnvelope serverResponse = theNetwork.getServerData();
//			theModel.updateGameModel(serverResponse);
			sendNextMove();
			fetchData();
		}
	}
	
	private void fetchData() {
		ServerDataEnvelope serverResponse = theNetwork.getServerData();
		gameOver = serverResponse.isGameOver();
		pathFinder.updateData(serverResponse.getPfData());
		mvcController.updateModel(serverResponse.getNewModel());
	}
	
	
	
	private void sendNextMove() {
		theNetwork.busyWaitForMyTurn();
		EMove nextMove = pathFinder.getNextMove();
		theNetwork.sendMove(nextMove);
	}

}





