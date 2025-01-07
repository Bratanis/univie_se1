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
//	public SquareGameMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
//		super (mapFields, startingCoordinates);
//	}
	
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
	public MapTerritories determineTerritories(Coordinates startingPosition) {
		
		MapTerritories territories = new MapTerritories();
		
		ETerritory topSide = ETerritory.TopLeftSide;
		ETerritory bottomSide = ETerritory.BottomSide;
		if (topSide.contains(startingPosition)) {
			territories.setMyTerritory(topSide);
			territories.setEnemyTerriotry(bottomSide);
		} else if (bottomSide.contains(startingPosition)) {
			territories.setMyTerritory(bottomSide);
			territories.setEnemyTerriotry(topSide);
		} else 
			throw new MapNavigationException ("Could not determine the territories of the players!");
			
		return territories;
	}

}