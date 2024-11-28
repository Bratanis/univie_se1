package client.network.servercompatlayer;

import client.customexceptions.IllegalConversionException;
import client.model.gamemap.ClientHalfMap;
import client.model.gamemap.GameMap;
import client.model.gamemap.mapelements.Coordinates;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

import java.util.*;

/**
 * 
 */
public class ClientToServerDataConverter {

	/**
	 * Default constructor
	 */
	public ClientToServerDataConverter() {
	}

	/**
	 * @param myPlayerID 
	 * @param localMap 
	 * @return
	 */
	public PlayerHalfMap getPlayerHalfMap(UniquePlayerIdentifier myPlayerID, GameMap localMap)  throws IllegalConversionException {
		Collection<PlayerHalfMapNode> phmNodes = new HashSet<PlayerHalfMapNode>();

		if (localMap.getClass() != ClientHalfMap.class) {
			throw new IllegalConversionException(
					"You need to input a local GameMap with type HALFMAP in order to get a server-readable PlayerHalfMap");
		}

		Coordinates localMapLastCoord = localMap.getLastCoordinates();
		Coordinates castleCoord = localMap.getMyStartingCoord();
		for (int x = 0; x <= localMapLastCoord.getX(); x++) {
			for (int y = 0; y <= localMapLastCoord.getY(); y++) {
				Coordinates target = new Coordinates(x, y);
				phmNodes.add(getPlayerHalfMapNode(target, localMap.getTerrainAt(target), castleCoord));
			}
		}
		assert (phmNodes.size() == 50);
		return new PlayerHalfMap(myPlayerID, phmNodes);
	}

	
	public static PlayerHalfMapNode getPlayerHalfMapNode(Coordinates nodeCoordinates, ETerrain terrain, Coordinates castleCoord) throws IllegalConversionException {

		return new PlayerHalfMapNode(nodeCoordinates.getX(), nodeCoordinates.getY(), nodeCoordinates.equals(castleCoord) , terrain);
	}


}