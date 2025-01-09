package client.mvc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.customexceptions.IllegalConversionException;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.GameMap;

/**
 * 
 */
public class GameModel {

	/**
	 * Attributes:
	 */
//	private boolean hasValidMap;
	private GameMap gameMap;
	
	// Game flags set to false and changed by the controller as the game progresses
	private boolean treasureCollected = false;
	private boolean gameWon = false;
	private boolean gameLost = false;
	
	private int round = 0;
	
	private PropertyChangeSupport support;
	
//	private GameProgress gameProgress;
		
//	private PathFinder pathFinder;
	
	//private PropertyChangeSupport support;
	
	private Logger logger;
	
//	private MvcNotificationCollector technicalData;

	/**
	 * Methods: 
	 */
	
//	private void setTechnicalData(MvcNotificationCollector technicalData) {
//		this.technicalData = technicalData;
//	}
	
	/**
	 * Returns true if the model has a gameMap set
	 * @return
	 */
	public boolean isValid() {
		return gameMap != null;
	}

	/**
	 * @param gameMap
	 * @param treasureCollected
	 * @param gameWon
	 * @param gameLost
	 */
	public GameModel(GameMap gameMap, boolean treasureCollected, boolean gameWon, boolean gameLost) {
		this.gameMap = gameMap;
		this.treasureCollected = treasureCollected;
		this.gameWon = gameWon;
		this.gameLost = gameLost;
		
		this.support = new PropertyChangeSupport(getClass());
	}

	public GameModel() {
		this.logger = LoggerFactory.getLogger(GameModel.class);
		this.support = new PropertyChangeSupport(getClass());
//		this.gameMap = generateValidGameMap(); 
//		this.gameProgress = new GameProgress(); // Initialize with an (empty) default game progress
//		this.pathFinder = PathFinder.getUndifinedInstance();
	}
	
	private boolean hasFullMap() {
		return (isValid() && gameMap.getClass() != ClientHalfMap.class);
	}
	
	public void setInitialHalfMap (ClientHalfMap initialHalfMap) {
		assert(!hasFullMap());
		updateGameMap (initialHalfMap);
	}
	
	private void updateGameMap (GameMap newMap) {
		
		if (hasFullMap() && gameMap.getClass() != newMap.getClass()) { 
			throw new IllegalConversionException(
				"Trying to set a map of type: " + newMap.getClass() + " where " + this.getClass() + " is expected!");
		} else {
		
		GameMap oldMap = gameMap;
		this.gameMap = newMap;
		support.firePropertyChange("Map Changed!", oldMap, newMap); // Make sure GameMap has equals() implemented!
		}
	}
	
	public void setTreasureCollected() {
		boolean oldVal = treasureCollected;
		treasureCollected = true;
		support.firePropertyChange("Treausre Collected!", oldVal, treasureCollected);
	}
	
	public void setGameWon() {
		boolean oldVal = gameWon;
		gameWon = true;
		support.firePropertyChange("Game Won!", oldVal, gameWon);
	}
	
	public void setGameLost() {
		boolean oldVal = gameLost;
		gameLost = true;
		support.firePropertyChange("Game Lost!", oldVal, gameLost);
	}
	
	public void nextRound() {
		int oldRound = round;
		++ round;
		support.firePropertyChange("Round Changed!", oldRound, round);
	}
	

	/**
	 * @return
	 */
//	private GameMap generateValidGameMap() {
//		MapGenerator generator = new MapGenerator();
//		MapValidator validator = new MapValidator();
//		ClientHalfMap testHalfMap = generator.offerHalfMap();
//
//		while (!validator.mapIsValid(testHalfMap)) {
////			logger.info("Generated an invalid map. Retrying...");
//			testHalfMap = generator.offerHalfMap();
//		}
//
//		return testHalfMap;
//	}

	/**
	 * @return
	 */
	public GameMap getGameMap() {
		return gameMap;
	}
//	
//	public void updateGameModel(ServerDataEnvelope gameData) {
//		logger.debug("Model received new game data: " + gameData);
////		gameProgress.updateGameProgress(gameData.getGameProgress());
//
//		GameMap newMap = gameData.getMapForClient();
//		if (this.gameMap.getClass() == ClientHalfMap.class) { // Will set the current half map to the new map,
//			setInitialFullMap(newMap);
//		} else {
//			this.gameMap.updateMapFields(newMap);  		  // or just update the fields if we already have a full map
//		}
////		logger.debug("New position according to gameProgress: " + gameProgress.getCurrentCoordinates());
//	}

	public void updateGameModel(GameModel newModel) {
		logger.debug("Model received new game data: " + newModel);
		
		updateGameMap(newModel.gameMap);
	
		if (newModel.treasureCollected) {
			setTreasureCollected();
		}
		if (newModel.gameWon) {
			setGameWon();
		}
		if (newModel.gameLost) {
			setGameLost();
		}
		nextRound();
	}
	/**
	 * @param newMap
	 */
//	private void setInitialFullMap(GameMap newMap) {
////		logger.debug("Attempting to update the client game map with: " + newMap);
//			newMap.setSupport(this.gameMap.getSupport());
//			gameMap = newMap;
////			gameMap.determineTerritories(gameProgress.getCurrentCoordinates()); // after setting the full map for the first time,
//																				// determine which territories belong to whom
////			this.pathFinder = new PathFinder(gameProgress, gameMap.getMyTerritory(), gameMap.getEnemyTerritory());
//	}
	
	
	

	/**
	 * @return
	 */
//	public EMove getNextMove() {
//		assert (pathFinder.isDefined());
//		if (pathFinder.noPendingMoves()) {
//			loadNextMoves();
//		}
//		return pathFinder.getNextMove();
//	}
	
//	private void loadNextMoves() {
//		Coordinates myCurrentCoordinates = gameProgress.getCurrentCoordinates();
//		Map<Coordinates, MapNode> surroundings = gameMap.getFieldsAround(myCurrentCoordinates);
//		pathFinder.loadNextMoves(surroundings, myCurrentCoordinates);
//	}


	/**
	 * @param listener
	 */
	public void addListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);	
	}

	public boolean isTreasureCollected() {
		return this.treasureCollected;
	}

	/**
	 * @param listener
	 */
//	public void addGameProgressListener(PropertyChangeListener listener) {
//		support.addPropertyChangeListener(listener);
//	}

	/**
	 * @return
	 */
//	public boolean gameIsOver() {
//		return gameProgress.gameIsOver();
//	}

}