package client.mvc.model.gamemap;

import java.util.HashMap;

import client.customexceptions.MapNavigationException;
import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;
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
//	public LongGameMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
//		super (mapFields, startingCoordinates);
//	}
	
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
	public MapTerritories determineTerritories(Coordinates startingPosition) {
		
		MapTerritories territories = new MapTerritories();
		
		ETerritory leftSide = ETerritory.TopLeftSide;
		ETerritory rightSide = ETerritory.RightSide;
		if (leftSide.contains(startingPosition)) {
			territories.setMyTerritory(leftSide);
			territories.setEnemyTerriotry(rightSide);
		} else if (rightSide.contains(startingPosition)) {
			territories.setMyTerritory(rightSide);
			territories.setEnemyTerriotry(leftSide);
		} else 
			throw new MapNavigationException ("Could not determine the territories of the players!");
		
		return territories;
	}
	

}