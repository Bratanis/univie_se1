package client.network.servercompatlayer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.customexceptions.IllegalConversionException;
import client.mvc.model.gamemap.GameMap;
import client.mvc.model.gamemap.LongGameMap;
import client.mvc.model.gamemap.SquareGameMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

/**
 * This class turns the data received from the server to a client-compatible class called ServerDataEnvelope
 */
public class ServerToClientDataConverter {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	private Coordinates myCurrentCoordinates = new Coordinates();
	
	private Coordinates myTreasureCoordinates = new Coordinates();
	private Coordinates firstCastleCoordinates = new Coordinates(); // Here we don't differentiate between my castle or the enemy
	private Coordinates secondCastleCoordinates = new Coordinates();// castle, we pin them both

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
	public ServerDataEnvelope getServerDataEnvelope(FullMap serverMap, boolean collectedTreasure, EPlayerGameState myWinOrLoss) {

		GameMap newGameMap = convertToLocalGameMap(serverMap);
		
//		GameProgress newGameProgress = determineGameProgress(newGameMap, collectedTreasure, myWinOrLoss); 
	
//		ServerDataEnvelope res = new ServerDataEnvelope(newGameMap, newGameProgress);
		
		// First make sure you have pinned your current position while extracting the map data
		assert (!myCurrentCoordinates.equals(new Coordinates()));
		ServerDataEnvelope newData = new ServerDataEnvelope.Builder(newGameMap, myCurrentCoordinates)
															.setTreasureCollected(collectedTreasure)
															.setWonGame(haveIWon(myWinOrLoss))
															.setLostGame(haveILost(myWinOrLoss))
															.build();
		
		logger.debug("new ModelDataEnvelope: " + newData);
		
		return newData;
	}
	
	private boolean haveIWon(EPlayerGameState myWinOrLoss) {
		return myWinOrLoss == EPlayerGameState.Won;
	}
	
	private boolean haveILost (EPlayerGameState myWinOrLoss) {
		return myWinOrLoss == EPlayerGameState.Lost;
	}

	
//	private GameProgress determineGameProgress (GameMap newGameMap, boolean collectedTreasure, EPlayerGameState myWinOrLoss) {
//	 
//		// First make sure you have pinned your current position while extracting the map data
//		assert (!myCurrentCoordinates.equals(new Coordinates()));
//		
//		boolean wonGame;
//		boolean lostGame;
//		if (myWinOrLoss == EPlayerGameState.Won) 
//			wonGame = true;
//		else 
//			wonGame = false;
//		if (myWinOrLoss == EPlayerGameState.Lost) {
//			lostGame = true;
//			
//		// TEMP
////			System.exit(0);
//		}
//			
//		else
//			lostGame = false;
//		
//		return new GameProgress(collectedTreasure, wonGame, lostGame, myCurrentCoordinates);
//	}
	
	

	private GameMap convertToLocalGameMap(FullMap serverMap) {

		HashMap<Coordinates, MapNode> localMapFields = getLocalMapFields(serverMap);
		
		GameMap result;

		if (localMapFields.containsKey(SquareGameMap.LAST_COORDINATES)) {
			result = new SquareGameMap(localMapFields);
		} else if (localMapFields.containsKey(LongGameMap.LAST_COORDINATES)) {
			result = new LongGameMap(localMapFields);
		} else {
			throw new IllegalConversionException("gameState does not contain a square or rectangular map; serverMap nodes: " + serverMap.getMapNodes());
		}
	
		markFieldsSurroundingKeyPositions(result);
		
		return result;
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
	
	// Has side effects: pins key positions while iterating through the elements (not optimal, but easy and efficient)
	private MapNode getLocalMapNode(FullMapNode sMapNode) {

		boolean hasTreasure = (sMapNode.getTreasureState() == ETreasureState.MyTreasureIsPresent);
		boolean hasCastle = (sMapNode.getFortState() == EFortState.MyFortPresent || sMapNode.getFortState() == EFortState.EnemyFortPresent);;
		boolean hasMe = (sMapNode.getPlayerPositionState().equals(EPlayerPositionState.MyPlayerPosition));
		boolean hasEnemy = (sMapNode.getPlayerPositionState().equals(EPlayerPositionState.EnemyPlayerPosition));
		
		if (sMapNode.getPlayerPositionState().equals(EPlayerPositionState.BothPlayerPosition)) {
			hasMe = true;
			hasEnemy = true;
		}

		MapNode localNode = new MapNode(sMapNode.getTerrain(), hasCastle, hasTreasure, hasMe, hasEnemy);
		
		if (hasMe)
			pinMyPosition(sMapNode);
		
		if(hasTreasure)
			pinTreasurePosition(sMapNode);
		
		if(hasCastle)
			pinCastlePositions(sMapNode);
		
		return localNode;
	}

	private void pinCastlePositions(FullMapNode sMapNode) {
		int myX = sMapNode.getX();
		int myY = sMapNode.getY();
		Coordinates someCastleCoord = new Coordinates (myX, myY);
		if (!firstCastleCoordinates.isValid()) {
			logger.debug("First castle at: " + someCastleCoord);			
			this.firstCastleCoordinates = someCastleCoord;
		} else if (!secondCastleCoordinates.isValid())
			this.secondCastleCoordinates = someCastleCoord;
	}

	private void pinTreasurePosition(FullMapNode sMapNode) {
		int myX = sMapNode.getX();
		int myY = sMapNode.getY();
		this.myTreasureCoordinates = new Coordinates (myX, myY);
	}

	// Helper function to pin my position for later
	private void pinMyPosition(FullMapNode sMapNode) {
		int myX = sMapNode.getX();
		int myY = sMapNode.getY();
		this.myCurrentCoordinates = new Coordinates (myX, myY);
	}
	
	private void markFieldsSurroundingKeyPositions (GameMap map) {
		if (myTreasureCoordinates.isValid()) {
			Map<Coordinates, MapNode> fieldsAroundTreasure = map.getFieldsAround(myTreasureCoordinates);
			fieldsAroundTreasure.forEach((coordinates, mapNode) -> {
				mapNode.setNearTreasure(true);
			});
		}
		
		if (firstCastleCoordinates.isValid()) {
			Map<Coordinates, MapNode> fieldsAroundFirstCastle = map.getFieldsAround(firstCastleCoordinates);
			fieldsAroundFirstCastle.forEach((coordinates, mapNode) -> {
				mapNode.setNearCastle(true);
			});
		}
		
		if (secondCastleCoordinates.isValid()) {
			Map<Coordinates, MapNode> fieldsAroundSecondCastle = map.getFieldsAround(secondCastleCoordinates);
			fieldsAroundSecondCastle.forEach((coordinates, mapNode) -> {
				mapNode.setNearCastle(true);
			});
		}
	}
}




