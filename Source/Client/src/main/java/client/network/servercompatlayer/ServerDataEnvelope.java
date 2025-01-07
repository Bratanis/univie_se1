package client.network.servercompatlayer;

import java.util.Map;

import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.mvc.model.GameModel;
import client.mvc.model.gamemap.GameMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

/**
 * A wrapper for the Data that the Client gets from the Server
 */
public class ServerDataEnvelope {

	/**
	 * Attributes:
	 */	
	private GameModel newModel;
	
	private PathFinderData pfData;
	
	private static boolean gameOver = false;
	

	
	/**
	 * Methods:
	 */
	 // Private constructor to enforce usage of the Builder
    private ServerDataEnvelope(Builder builder) {
        this.newModel = builder.newModel;
        this.pfData = builder.pfData;
    }

    /**
     * Getters for attributes (optional, based on use case).
     */
    public GameModel getNewModel() {
        return newModel;
    }

    public PathFinderData getPfData() {
        return pfData;
    }

    /**
     * Builder Class for ServerDataEnvelope.
     */
    public static class Builder {

        private GameModel newModel;
        private PathFinderData pfData;

        private final GameMap mapForClient;
        private final Coordinates myPosition;

        private boolean treasureCollected = false;
        private boolean wonGame = false;
        private boolean lostGame = false;

        public Builder(GameMap mapForClient, Coordinates myPosition) {
            if (mapForClient == null || myPosition == null) {
                throw new IllegalArgumentException("mapForClient and myPosition cannot be null");
            }
            this.mapForClient = mapForClient;
            this.myPosition = myPosition;
        }

        public Builder setTreasureCollected(boolean treasureCollected) {
            this.treasureCollected = treasureCollected;
            return this;
        }

        public Builder setWonGame(boolean wonGame) {
            this.wonGame = wonGame;
            if(wonGame)
            	gameOver = true;
            return this;
        }

        public Builder setLostGame(boolean lostGame) {
            this.lostGame = lostGame;
            if(lostGame)
            	gameOver = true;
            return this;
        }

        public ServerDataEnvelope build() {
            this.newModel = new GameModel(mapForClient, treasureCollected, wonGame, lostGame);

            Map<Coordinates, MapNode> surroundings = mapForClient.getFieldsAround(myPosition);
            this.pfData = new PathFinderData(myPosition, surroundings, treasureCollected);

            return new ServerDataEnvelope(this);
        }
    }
	
//	public ServerDataEnvelope(GameMap mapForClient, Coordinates myPosition, 
//							  boolean treasureCollected, boolean wonGame, boolean lostGame) {
//		this.newModel = new GameModel(mapForClient, treasureCollected, wonGame, lostGame);
//		
//		Map<Coordinates, MapNode> surroundings = mapForClient.getFieldsAround(myPosition);
//		this.pfData = new PathFinderData(myPosition, surroundings, treasureCollected);
//	}
	
	

	public String toString() {
		return ("{ " + newModel.toString() + pfData.toString() + " }");
	}

	public MapTerritories getTerritories() {
		return newModel.getGameMap().determineTerritories(pfData.getMyPosition());
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

}