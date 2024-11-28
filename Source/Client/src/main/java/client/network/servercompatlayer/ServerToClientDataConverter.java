package client.network.servercompatlayer;

import java.io.*;
import java.util.*;

import client.customexceptions.IllegalConversionException;
import client.model.GameProgress;
import client.model.gamemap.GameMap;
import client.model.gamemap.LongGameMap;
import client.model.gamemap.SquareGameMap;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
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
public class ServerToClientDataConverter {
	private Coordinates myCurrentCoordinates = new Coordinates();

	/**
	 * Default constructor
	 */
	public ServerToClientDataConverter() {
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
	 
		// First make sure you have pinned your current position while extracting the map data
		assert (myCurrentCoordinates.equals(new Coordinates()));
		
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
	
	

	private GameMap convertToLocalGameMap(FullMap serverMap) {

		HashMap<Coordinates, MapNode> localMapFields = getLocalMapFields(serverMap);

		if (localMapFields.containsKey(SquareGameMap.LAST_COORDINATES)) {
			return new SquareGameMap(localMapFields);
		} else if (localMapFields.containsKey(LongGameMap.LAST_COORDINATES)) {
			return new LongGameMap(localMapFields);
		} else {
			throw new IllegalConversionException("gameState does not contain a square or rectangular map; serverMap nodes: " + serverMap.getMapNodes());
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
	
	// Has side effects: pins my position while iterating through the elements (not optimal, but easy and efficient)
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
		
		if (hasMe)
			pinMyPosition(sMapNode);
		
		return localNode;
	}

	// Helper function to pin my position for later
	private void pinMyPosition(FullMapNode sMapNode) {
		int myX = sMapNode.getX();
		int myY = sMapNode.getY();
		this.myCurrentCoordinates = new Coordinates (myX, myY);
	}
	
}