package client.model.gamemap;

import java.util.*;

import client.customexceptions.MapNavigationException;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.ETerritory;
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

	@Override
	public void determineTerritories(Coordinates startingPosition) {
		ETerritory leftSide = ETerritory.TopLeftSide;
		ETerritory rightSide = ETerritory.RightSide;
		if (leftSide.contains(startingPosition)) {
			myTerritory = leftSide;
			enemyTerritory = rightSide;
		} else if (rightSide.contains(startingPosition)) {
			myTerritory = rightSide;
			enemyTerritory = leftSide;
		} else 
			throw new MapNavigationException ("Could not determine the territories of the players!");
	}

}