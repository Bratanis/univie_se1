package client.model.gamemap;

import java.util.HashMap;

import client.customexceptions.MapNavigationException;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.ETerritory;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class ClientHalfMap extends GameMap {
	
	public static final Coordinates LAST_COORDINATES = new Coordinates (9, 4);

	/**
	 * Default constructor
	 */
	public ClientHalfMap(HashMap<Coordinates, MapNode> mapFields, Coordinates startingCoordinates) {
		super(mapFields, startingCoordinates);
	}

	public ClientHalfMap() {
		super();
	}

	/**
	 * Liskov!!!
	 * @return
	 */
	public EMove findEnemyDirection() {
		throw new MapNavigationException("Only local client half map is available -> No enemy territory!");
	}

	@Override
	public Coordinates getLastCoordinates() {
		assert(this.mapFields.containsKey(LAST_COORDINATES));
		return LAST_COORDINATES; 
	}

	// Unlikely to be used
	@Override
	public void determineTerritories(Coordinates startingPosition) {
		myTerritory = ETerritory.TopLeftSide;
		enemyTerritory = ETerritory.None;
		
	}
														
}