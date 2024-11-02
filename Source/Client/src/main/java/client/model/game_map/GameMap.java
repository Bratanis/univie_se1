package Model.GameMap;

import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class GameMap {

	/**
	 * Default constructor
	 */
	public GameMap() {
	}

	/**
	 * 
	 */
	private HashMap<Coordinates, MapNode> mapFields;

	/**
	 * 
	 */
	private EMapType mapType;

	/**
	 * 
	 */
	private Coordinates myStartingPos;

	/**
	 * 
	 */
	private PropertyChangeSupport support;

	/**
	 * 
	 */
	private static Coordinate lastCoordinate;

	/**
	 * @param mapFields 
	 * @param type 
	 * @param startingCoordinates
	 */
	public GameMap(HashMap mapFields, EMapType type, Coordinates startingCoordinates) {
		// TODO implement here
	}

	/**
	 * @param newMap
	 */
	public void setNewGameMap(GameMap newMap) {
		// TODO implement here
	}

	/**
	 * @param listener
	 */
	public void addListener(PropertyChangeListener listener ) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public abstract EMove findEnemyDirection();

	/**
	 * @param centre 
	 * @return
	 */
	public HashMap<Coordinates, MapNode> getFieldsAround(Coordinates centre) {
		// TODO implement here
		return null;
	}

}