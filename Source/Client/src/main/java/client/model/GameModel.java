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
		//this.hasValidMap = false;
		this.gameMap = generateValidGameMap(); 
		this.gameProgress = new GameProgress(); // Initialize with an (empty) default game progress
		this.pathFinder = new PathFinder(gameProgress);
	//	this.support = new PropertyChangeSupport(this);
	}
	
	//public void generateInitialHalfMap() {
		//GameMap validClientHalfMap = generateValidGameMap();
		//updateOrSetGameMap(validClientHalfMap);
		//this.hasValidMap = true;
	//}

	/**
	 * @return
	 */
	private GameMap generateValidGameMap() {
		MapGenerator generator = new MapGenerator();
		MapValidator validator = new MapValidator();
		ClientHalfMap testHalfMap = generator.offerHalfMap();

		while (!validator.mapIsValid(testHalfMap)) {
			logger.info("Generated an invalid map. Retrying...");
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

		// Handle the map update
		//GameMap oldGameMap = this.gameMap;
		updateOrSetGameMap(gameData.getMapForClient());
		//support.firePropertyChange("gameMap", oldGameMap, this.gameMap);

		// Handle the game progress update
		//GameProgress oldGameProgress = this.gameProgress;
		this.gameProgress.updateGameProgress(gameData.getGameProgress());
		logger.debug("New position according to gameProgress: " + gameProgress.getCurrentCoordinates());
		//support.firePropertyChange("gameProgress", oldGameProgress, this.gameProgress);
	}

	/**
	 * @param newMap
	 */
	private void updateOrSetGameMap(GameMap newMap) {
		logger.debug("Attempting to update the client game map with: " + newMap);
		if (this.gameMap.getClass() == ClientHalfMap.class) { // Will set the current half map to the new map, or just update the fields if we already have a full map
			newMap.setSupport(this.gameMap.getSupport());
			this.gameMap = newMap;
		} else {
			this.gameMap.updateMapFields(newMap);
		}
	}

	/**
	 * @return
	 */
	public EMove getNextMove() {
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