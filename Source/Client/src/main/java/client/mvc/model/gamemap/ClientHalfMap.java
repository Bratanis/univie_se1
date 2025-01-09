package client.mvc.model.gamemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public class ClientHalfMap extends GameMap {
	
	public static final Coordinates LAST_COORDINATES = new Coordinates (9, 4);

	/**
	 * Default constructor
	 */
	public ClientHalfMap(Map<Coordinates, MapNode> mapFields) {
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

	public Set<Coordinates> getAllCoordinates() {
		return mapFields.keySet();
	}

	// Used for testing
	public static Map<Coordinates, MapNode> getGrassOnlyMapFields(){
		
		int lastX = ClientHalfMap.LAST_COORDINATES.getX();
		int lastY = ClientHalfMap.LAST_COORDINATES.getY();
	
		Map<Coordinates, MapNode> grassOnlyMap = new HashMap<>();
		
		for (int currentX = 0; currentX <= lastX; ++ currentX) {
			for (int currentY = 0; currentY <= lastY; ++ currentY) {
				Coordinates fieldCoordinates = new Coordinates(currentX, currentY);
				MapNode fieldNode = new MapNode(ETerrain.Grass);
				grassOnlyMap.put(fieldCoordinates, fieldNode);
			}
		}
		
		return grassOnlyMap;
	}
	
														
}