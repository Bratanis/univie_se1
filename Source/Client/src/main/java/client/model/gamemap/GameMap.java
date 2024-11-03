package client.model.gamemap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

import messagesbase.messagesfromclient.EMove;

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
	private Coordinates myStartingPos;

	/**
	 * 
	 */
	private PropertyChangeSupport support;


	
	/**
	 * @param mapFields
	 * @param startingCoordinates
	 */
	public GameMap(HashMap mapFields, Coordinates startingCoordinates) {
		this.mapFields = mapFields;
		this.myStartingPos = startingCoordinates;
	}
	
	public GameMap(HashMap mapFields) {
		this (mapFields, new Coordinates(-1, -1));
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