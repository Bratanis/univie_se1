package ClientNetwork;

import Model.GameMap.GameMap;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ClientNetwork {

	/**
	 * Default constructor
	 */
	public ClientNetwork() {
	}

	/**
	 * 
	 */
	private UniquePlayerIdentifier myPlayerID;

	/**
	 * 
	 */
	private UniqueGameIdentifier currentGameID;

	/**
	 * 
	 */
	private WebClient baseWebClient;

	/**
	 * 
	 */
	private GameState cachedGameState;

	/**
	 * 
	 */
	private NetworkToClientDataConverter fromServer;

	/**
	 * 
	 */
	private ClientToNetworkDataConverter toServer;

	/**
	 * @param serverBaseUrl 
	 * @param currentGameID
	 */
	public ClientNetwork(Url serverBaseUrl, GameID currentGameID) {
		// TODO implement here
	}

	/**
	 * 
	 */
	private void requestPlayerID() {
		// TODO implement here
	}

	/**
	 * @param localHalfMap
	 */
	public void sendLocalMapToServer(GameMap localHalfMap) {
		// TODO implement here
	}

	/**
	 * 
	 */
	private void delayRequest() {
		// TODO implement here
	}

	/**
	 * 
	 */
	private void updateCachedGameState() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	private boolean clientMustAct() {
		// TODO implement here
		return false;
	}

	/**
	 * @return
	 */
	public ModelDataEnvelope getModelData() {
		// TODO implement here
		return null;
	}

	/**
	 * @param moveDir
	 */
	public void sendMove(EMove moveDir) {
		// TODO implement here
	}

	/**
	 * 
	 */
	public void busyWaitForServer() {
		// TODO implement here
	}

}