package client.network.servercompatlayer;

import java.util.Collection;
import java.util.HashSet;

import client.customexceptions.IllegalConversionException;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.GameMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

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
					"You need to input a local ClientHalfMap in order to get a server-readable PlayerHalfMap");
		}

		Coordinates localMapLastCoord = localMap.getLastCoordinates();
		for (int x = 0; x <= localMapLastCoord.getX(); x++) {
			for (int y = 0; y <= localMapLastCoord.getY(); y++) {
				Coordinates fieldCoordinates = new Coordinates(x, y);
				phmNodes.add(getPlayerHalfMapNode(fieldCoordinates, localMap.getNodeAt(fieldCoordinates)));
			}
		}
		assert (phmNodes.size() == 50);
		return new PlayerHalfMap(myPlayerID, phmNodes);
	}

	
	public static PlayerHalfMapNode getPlayerHalfMapNode(Coordinates fieldCoordinates, MapNode fieldNode) throws IllegalConversionException {

		return new PlayerHalfMapNode(fieldCoordinates.getX(), fieldCoordinates.getY(), fieldNode.hasCastle() , fieldNode.getTerrain());
	}


}