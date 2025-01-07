package client.mvc.model.gamemap;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public abstract class GameMap {
	
	/**
	 * Attributes:
	 */
	private static final Logger logger = LoggerFactory.getLogger(GameMap.class);
	
	protected HashMap<Coordinates, MapNode> mapFields;
	
//	protected ETerritory myTerritory = ETerritory.None;		// Not useable for the abstract parent class
//	protected ETerritory enemyTerritory = ETerritory.None; // Will make sense for the actual implementing classes (parent class abstract anyway

//	private PropertyChangeSupport support;


	
	/**
	 * @param mapFields
	 * @param startingCoordinates
	 */
//	public GameMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
//		this.mapFields = mapFields;
////		this.support = new PropertyChangeSupport(this);
//	}
	
	public GameMap(HashMap<Coordinates, MapNode> mapFields) {
//		this (mapFields, new Coordinates(-1, -1));
		this.mapFields = mapFields;
	}
	
	public GameMap() {
		this (new HashMap<>());
	}


//	/**
//	 * @Override
//	 * @param newMap
//	 */
//	public void updateMapFields(GameMap newMap) {
//		if (this.getClass() != newMap.getClass()) { 
//			throw new IllegalConversionException(
//				"Trying to set a map of type: " + newMap.getClass() + " where " + this.getClass() + " is expected!");
//		}
//		HashMap<Coordinates, MapNode> oldFields = this.mapFields;
//		if(oldFields.equals(newMap.mapFields))
//			logger.info("No new map yet...");
//		else {
//			this.mapFields = new HashMap<>(newMap.mapFields);
//		
//			support.firePropertyChange("updateMapFields", oldFields, this);	// Maybe send only the map fields instead!
//		}
//	}

	public abstract MapTerritories determineTerritories(Coordinates startingPosition);

	/**
	 * @param listener
	 */
//	public void addListener(PropertyChangeListener listener ) {
//		support.addPropertyChangeListener(listener);
//	}
	
//	public PropertyChangeSupport getSupport() {
//		return this.support;
//	}

//	public void setSupport(PropertyChangeSupport newSupport ) {
//		this.support = newSupport;
//		support.firePropertyChange("A full map from the server has been set!", null, this);
//	}
	/**
	 * @return
	 */
//	public abstract EMove findEnemyDirection();
	
	public abstract Coordinates getLastCoordinates();

	/**
	 * @param centre 
	 * @return
	 */
	public Map<Coordinates, MapNode> getFieldsAround(Coordinates centre) {

		Map<Coordinates, MapNode> surroundings = new HashMap<>();

		
		//left
		addFieldToThe(surroundings, centre, EMove.Left);
		
		//right
		addFieldToThe(surroundings, centre, EMove.Right);
		
		//top
		addFieldToThe(surroundings, centre, EMove.Up);
	
		//bottom
		addFieldToThe(surroundings, centre, EMove.Down);

		assert (!surroundings.isEmpty());
		
		return surroundings;
	}
	
	/**
	 * Helper function for getFieldsAround() method
	 * @param surroundings
	 * @param direction
	 */
	private void addFieldToThe(Map<Coordinates, MapNode> surroundings, Coordinates centre, EMove direction) {
		Coordinates coordToTheLeft = centre.getCoordinatesToThe(direction);
		MapNode mapNode = this.getNodeAt(coordToTheLeft);
		if (mapNode != null)
			surroundings.put(coordToTheLeft, mapNode);
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

//	public ETerritory getEnemyTerritory() {
//		return enemyTerritory;
//	}
//
//	public ETerritory getMyTerritory() {
//		return myTerritory;
//	}

}