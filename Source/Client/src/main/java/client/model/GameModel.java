package client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import client.model.gamemap.ClientHalfMap;
import client.model.gamemap.GameMap;
import client.model.gamemap.creation.MapGenerator;
import client.model.gamemap.creation.MapValidator;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.PathFinder;
import client.network.servercompatlayer.ModelDataEnvelope;
import messagesbase.messagesfromclient.EMove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class GameModel {

	/**
	 * Attributes:
	 */
	private boolean hasValidMap;
	private GameMap gameMap;
	private GameProgress gameProgress;
	private PathFinder pathFinder;
	
	//private PropertyChangeSupport support;
	
	private Logger logger;
	

	/**
	 * Methods: 
	 */
	public GameModel() {
		this.logger = LoggerFactory.getLogger(GameModel.class);
		this.gameMap = generateValidGameMap(); 
		this.gameProgress = new GameProgress(); // Initialize with an (empty) default game progress
		this.pathFinder = PathFinder.getUndifinedInstance();
	}
	

	/**
	 * @return
	 */
	private GameMap generateValidGameMap() {
		MapGenerator generator = new MapGenerator();
		MapValidator validator = new MapValidator();
		ClientHalfMap testHalfMap = generator.offerHalfMap();

		while (!validator.mapIsValid(testHalfMap)) {
//			logger.info("Generated an invalid map. Retrying...");
			testHalfMap = generator.offerHalfMap();
		}

		return testHalfMap;
	}

	/**
	 * @return
	 */
	public GameMap getGameMap() {
		return gameMap;
	}
	
	public void updateGameModel(ModelDataEnvelope gameData) {
		logger.debug("Model received new game data: " + gameData);
		gameProgress.updateGameProgress(gameData.getGameProgress());

		GameMap newMap = gameData.getMapForClient();
		if (this.gameMap.getClass() == ClientHalfMap.class) { // Will set the current half map to the new map,
			setInitialFullMap(newMap);
		} else {
			this.gameMap.updateMapFields(newMap);  		  // or just update the fields if we already have a full map
		}
//		logger.debug("New position according to gameProgress: " + gameProgress.getCurrentCoordinates());
	}

	/**
	 * @param newMap
	 */
	private void setInitialFullMap(GameMap newMap) {
//		logger.debug("Attempting to update the client game map with: " + newMap);
			newMap.setSupport(this.gameMap.getSupport());
			gameMap = newMap;
			gameMap.determineTerritories(gameProgress.getCurrentCoordinates()); // after setting the full map for the first time,
																				// determine which territories belong to whom
			this.pathFinder = new PathFinder(gameProgress, gameMap.getMyTerritory(), gameMap.getEnemyTerritory());
	}
	
	
	

	/**
	 * @return
	 */
	public EMove getNextMove() {
		assert (pathFinder.isDefined());
		if (pathFinder.noPendingMoves()) {
			loadNextMoves();
		}
		return pathFinder.getNextMove();
	}
	
	private void loadNextMoves() {
		Coordinates myCurrentCoordinates = gameProgress.getCurrentCoordinates();
		Map<Coordinates, MapNode> surroundings = gameMap.getFieldsAround(myCurrentCoordinates);
		pathFinder.loadNextMoves(surroundings, myCurrentCoordinates);
	}


	/**
	 * @param listener
	 */
	public void addGameMapListener(PropertyChangeListener listener) {
		gameMap.addListener(listener);
	}

	/**
	 * @param listener
	 */
	public void addGameProgressListener(PropertyChangeListener listener) {
		gameProgress.addListener(listener);
	}

	/**
	 * @return
	 */
	public boolean gameIsOver() {
		return gameProgress.gameIsOver();
	}

}