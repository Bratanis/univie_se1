package client.model.gamemap;

import java.util.*;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class LongGameMap extends GameMap {
	
	public static final Coordinates LAST_COORDINATES = new Coordinates (19, 4);

	public Coordinates getLastCoordinates() {
		assert(this.mapFields.containsKey(LAST_COORDINATES));
		return LAST_COORDINATES; 
	}
	
	/**
	 * @param mapFields
	 * @param startingCoordinates
	 */
	public LongGameMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
		super (mapFields, startingCoordinates);
	}
	
	public LongGameMap(HashMap<Coordinates, MapNode> mapFields) {
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