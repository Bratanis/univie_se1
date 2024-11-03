package client.network.servercompatlayer;

import client.model.gamemap.GameMap;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ClientToNetworkDataConverter {

	/**
	 * Default constructor
	 */
	public ClientToNetworkDataConverter() {
	}

	/**
	 * @param myPlayerID 
	 * @param localMap 
	 * @return
	 */
	public PlayerHalfMap getPlayerHalfMap(UniquePlayerIdentifier myPlayerID, GameMap localMap) {
		// TODO implement here
		return null;
	}

}