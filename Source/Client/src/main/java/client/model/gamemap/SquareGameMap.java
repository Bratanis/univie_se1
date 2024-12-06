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

	@Override
	public void determineTerritories(Coordinates startingPosition) {
		ETerritory topSide = ETerritory.TopLeftSide;
		ETerritory bottomSide = ETerritory.BottomSide;
		if (topSide.contains(startingPosition)) {
			myTerritory = topSide;
			enemyTerritory = bottomSide;
		} else if (bottomSide.contains(startingPosition)) {
			myTerritory = bottomSide;
			enemyTerritory = topSide;
		} else 
			throw new MapNavigationException ("Could not determine the territories of the players!");
			
		
	}

}