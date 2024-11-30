package client.model.pathfinder;

import java.io.*;
import java.util.*;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public class SearchHalfMapState extends NavigationState {


	public SearchHalfMapState(GameProgress gameProgress) {
		super(gameProgress);
	}

	/**
	 * goal is either finding the treasure or finding the castle (game over)
	 */
	@Override
	public boolean goalComplete() {
		return (gameProgress.gameIsOver() || gameProgress.treasureCollected());
	}

	
	// TO BE REFACTORED: Split into 2 parts
	@Override
	public Collection<EMove> determineNextMoves (Map<Coordinates, MapNode> surroundings) {
		
		// First add my current position to the list of places I have already been
		visitedCoordinates.add(gameProgress.getCurrentCoordinates());
		
		//Declare the coordinates where we want to go
		Coordinates destination = new Coordinates();
		
		// Then consider all of the nodes I can get to:
		for (Map.Entry<Coordinates, MapNode> entry : surroundings.entrySet()) {
			
			Coordinates coordinates = entry.getKey();
			MapNode mapNode = entry.getValue();
			
		    if (mapNode.hasTreasure() && !gameProgress.treasureCollected()) { // go if there is a treasure that hasn't been collected
		        destination = coordinates;
		        break;
		        
		    } else if (mapNode.hasCastle() && gameProgress.treasureCollected()) { // go if treasure has been collected and there is a castle
		        destination = coordinates;
		        break;
		        
		    } else if (mapNode.isNearTreasure()) { // go if it's near a treasure
		    	destination = coordinates;
		    	break;
		    	
		    } else if (!visitedCoordinates.contains(coordinates)) {
		    	
		    	if(mapNode.getTerrain() == ETerrain.Mountain) {
		    		destination = coordinates;
		    		break;
		    	} else if (mapNode.getTerrain() == ETerrain.Grass) {
		    		destination = coordinates;
		    		break;
		    	} 
		    } else if (mapNode.getTerrain() != ETerrain.Water) {
		    	destination = coordinates;
		    }
		}
		return gameProgress.getCurrentCoordinates().directionTo(destination);
	}
	
}