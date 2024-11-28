package client.model.gamemap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.customexceptions.IllegalConversionException;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public abstract class GameMap {
	
	/**
	 * Attributes:
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected HashMap<Coordinates, MapNode> mapFields;
	private Coordinates myStartingCoord;

	private PropertyChangeSupport support;

	
	/**
	 * @param mapFields
	 * @param startingCoordinates
	 */
	public GameMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
		this.mapFields = mapFields;
		this.myStartingCoord = startingCoordinates;
		this.support = new PropertyChangeSupport(this);
	}
	
	public GameMap(HashMap<Coordinates, MapNode> mapFields) {
		this (mapFields, new Coordinates(-1, -1));
	}
	
	public GameMap() {
		this (new HashMap<>());
	}

	public Coordinates getMyStartingCoord() {
		return myStartingCoord;
	}

	public void setMyStartingCoord(Coordinates myStartingCoord) {
		this.myStartingCoord = myStartingCoord;
	}

	/**
	 * @Override
	 * @param newMap
	 */
	public void updateMapFields(GameMap newMap) {
		if (this.getClass() != newMap.getClass()) { 
			throw new IllegalConversionException(
				"Trying to set a map of type: " + newMap.getClass() + " where " + this.getClass() + " is expected!");
		}
		HashMap<Coordinates, MapNode> oldFields = this.mapFields;
		this.mapFields = new HashMap<>(newMap.mapFields);
		
		support.firePropertyChange("updateMapFields", oldFields, this.mapFields);	}


	/**
	 * @param listener
	 */
	public void addListener(PropertyChangeListener listener ) {
		support.addPropertyChangeListener(listener);
	}
	
	public PropertyChangeSupport getSupport() {
		return this.support;
	}

	public void setSupport(PropertyChangeSupport newSupport ) {
		this.support = newSupport;
		support.firePropertyChange("A full map from the server has been set!", null, this);
	}
	/**
	 * @return
	 */
	public abstract EMove findEnemyDirection();
	
	public abstract Coordinates getLastCoordinates();

	/**
	 * @param centre 
	 * @return
	 */
	public HashMap<Coordinates, MapNode> getFieldsAround(Coordinates centre) {
		// TODO implement here
		return null;
	}
	
	public ETerrain getTerrainAt (Coordinates targetCoordinates) {
		MapNode targetNode = getNodeAt(targetCoordinates);
		if (targetNode != null)
			return targetNode.getTerrain();
		else {
			//logger.warn("Tried to get terrain of coordinates that are out of scope! (returning water)");
			return ETerrain.Water; // If the Coordinates are out of the scope of the map, 
		}
									//return a field you cannot walk on (imaginary water border around the map).
	}

	public MapNode getNodeAt(Coordinates targetCoordinates) {
		return  mapFields.get(targetCoordinates);
	}

}