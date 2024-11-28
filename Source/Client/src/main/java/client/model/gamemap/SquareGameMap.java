package client.model.gamemap;

import java.util.*;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class SquareGameMap extends GameMap {

	public static final Coordinates LAST_COORDINATES = new Coordinates (9, 9);

	
	public Coordinates getLastCoordinates() {
		assert(this.mapFields.containsKey(LAST_COORDINATES));
		return LAST_COORDINATES; 
	}
	
	/**
	 * @param mapFields
	 * @param startingCoordinates
	 */
	public SquareGameMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
		super (mapFields, startingCoordinates);
	}
	
	public SquareGameMap(HashMap<Coordinates, MapNode> mapFields) {
		super (mapFields);
	}


	/**
	 * @Override
	 * @return
	 */
	public EMove findEnemyDirection() {
		return null;
	}

}