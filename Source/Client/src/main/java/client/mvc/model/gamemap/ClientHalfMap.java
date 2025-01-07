package client.mvc.model.gamemap;

import java.util.HashMap;

import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;

/**
 * 
 */
public class ClientHalfMap extends GameMap {
	
	public static final Coordinates LAST_COORDINATES = new Coordinates (9, 4);

	/**
	 * Default constructor
	 */
	public ClientHalfMap(HashMap<Coordinates, MapNode> mapFields) {
		super(mapFields);
	}

	public ClientHalfMap() {
		super();
	}

//	/**
//	 * Liskov!!!
//	 * @return
//	 */
//	public EMove findEnemyDirection() {
//		throw new MapNavigationException("Only local client half map is available -> No enemy territory!");
//	}

	@Override
	public Coordinates getLastCoordinates() {
		assert(this.mapFields.containsKey(LAST_COORDINATES));
		return LAST_COORDINATES; 
	}

	// Unlikely to be used
	@Override
	public MapTerritories determineTerritories(Coordinates startingPosition) {
		MapTerritories territories = new MapTerritories();

		territories.setMyTerritory(ETerritory.TopLeftSide);
		territories.setEnemyTerriotry(ETerritory.None);
		return territories;
	}

	
														
}