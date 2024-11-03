package client.network.servercompatlayer;

import client.model.gamemap.GameMap;
import client.model.GameProgress;

import java.io.*;
import java.util.*;

/**
 * A wrapper for the Data that the Client gets from the Server
 */
public class ModelDataEnvelope {

	/**
	 * Attributes:
	 */	
	private GameMap mapForClient;
	private GameProgress gameProgress;

	
	/**
	 * Methods:
	 */
	
	public ModelDataEnvelope(GameMap mapForClient, GameProgress gameProgress) {
		this.mapForClient = mapForClient;
		this.gameProgress = gameProgress;
	}


	public GameMap getMapForClient() {
		return mapForClient;
	}


	public GameProgress getGameProgress() {
		return gameProgress;
	}


}