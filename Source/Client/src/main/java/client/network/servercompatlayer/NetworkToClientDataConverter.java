package client.network.servercompatlayer;

import java.io.*;
import java.util.*;

import client.customexceptions.IllegalConversionException;
import client.model.GameProgress;
import client.model.gamemap.Coordinates;
import client.model.gamemap.GameMap;
import client.model.gamemap.LongGameMap;
import client.model.gamemap.MapNode;
import client.model.gamemap.SquareGameMap;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.GameState;

/**
 * 
 */
public class NetworkToClientDataConverter {
	

	/**
	 * Default constructor
	 */
	public NetworkToClientDataConverter() {
	}

	/**
	 * @param serverMap
	 * @param collectedTreasure
	 * @param myWinOrLoss
	 * @return
	 */
	public ModelDataEnvelope getModelDataEnvelope(FullMap serverMap, boolean collectedTreasure, EPlayerGameState myWinOrLoss) {

		GameMap newGameMap = convertToLocalGameMap(serverMap);
		
		GameProgress newGameProgress = determineGameProgress(newGameMap, collectedTreasure, myWinOrLoss); 
		
		return new ModelDataEnvelope(newGameMap, newGameProgress);
	}
	
	private GameProgress determineGameProgress (GameMap newGameMap, boolean collectedTreasure, EPlayerGameState myWinOrLoss) {

		Coordinates myCurrentCoordinates = determineMyCoordinates(newGameMap);
		
		boolean wonGame;
		boolean lostGame;
		if (myWinOrLoss == EPlayerGameState.Won) 
			wonGame = true;
		else 
			wonGame = false;
		if (myWinOrLoss == EPlayerGameState.Lost)
			lostGame = true;
		else
			lostGame = false;
		
		return new GameProgress(collectedTreasure, wonGame, lostGame, myCurrentCoordinates);
	}
	
	private Coordinates determineMyCoordinates(GameMap newGameMapForClient) {
		return null;
	};

	GameMap convertToLocalGameMap(FullMap serverMap) {

		HashMap<Coordinates, MapNode> localMapFields = getLocalMapFields(serverMap);

		if (localMapFields.containsKey(SquareGameMap.getLastCoordinates())) {
			return new SquareGameMap(localMapFields);
		} else if (localMapFields.containsKey(LongGameMap.getLastCoordinates())) {
			return new LongGameMap(localMapFields);
		} else {
			throw new IllegalConversionException("gameState does not contain a square or rectangular map!");
		}
	}
	
	private HashMap<Coordinates, MapNode> getLocalMapFields(FullMap serverMap) {

		HashMap<Coordinates, MapNode> fields = new HashMap<Coordinates, MapNode>();

		for (FullMapNode serverMapNode : serverMap.getMapNodes()) {

			Coordinates fieldCoordinates = new Coordinates(serverMapNode.getX(), serverMapNode.getY());

			MapNode fieldMapNode = getLocalMapNode(serverMapNode);
				
			fields.put(fieldCoordinates, fieldMapNode);
		}

		return fields;
	}
	
	private MapNode getLocalMapNode(FullMapNode sMapNode) {

		boolean hasTreasure = (sMapNode.getTreasureState() == ETreasureState.MyTreasureIsPresent);
		boolean hasCastle = (sMapNode.getFortState() == EFortState.MyFortPresent || sMapNode.getFortState() == EFortState.EnemyFortPresent);;
		boolean hasMe = (sMapNode.getPlayerPositionState().equals(EPlayerPositionState.MyPlayerPosition));
		boolean hasEnemy = (sMapNode.getPlayerPositionState().equals(EPlayerPositionState.EnemyPlayerPosition));
		
		if (sMapNode.getPlayerPositionState().equals(EPlayerPositionState.BothPlayerPosition)) {
			hasMe = true;
			hasEnemy = true;
		}

		MapNode localNode = new MapNode(sMapNode.getTerrain(), hasTreasure, hasCastle, hasMe, hasEnemy);
		
		return localNode;
	}
	
}