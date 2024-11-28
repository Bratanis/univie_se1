package client.model.gamemap.creation;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.model.gamemap.ClientHalfMap;
import client.model.gamemap.mapelements.Coordinates;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public class MapValidator {
	
	private final Logger logger;

	/**
	 * Default constructor
	 */
	public MapValidator() {
		this.logger = LoggerFactory.getLogger(MapValidator.class);
	}

	/**
	 * @param testMap 
	 * @return
	 */
	public boolean mapIsValid(ClientHalfMap testMap) {
		boolean noIslands = hasNoIslands(testMap);
		boolean edgesOk = waterOnEdgesLeqHalf(testMap);
		if (!noIslands)
			logger.info("The map has islans");
		if (!edgesOk)
			logger.info("Map edges have too much water!");
		return (noIslands && edgesOk); 
	}

	/**
	 * Map can have max 2 water on the short sides and max 4 water on the long sides
	 * @param gameMap
	 * @return
	 */
	private boolean waterOnEdgesLeqHalf(ClientHalfMap gameMap) {
		
		return (checkTopAndBottomForWater(gameMap) && checkLeftAndRightForWater(gameMap));
	}


	// Helper function for waterOnEdgesLeqHalf
	private boolean checkTopAndBottomForWater(ClientHalfMap gameMap) {
		
		int firstX = 0; // column
		int lastX = gameMap.getLastCoordinates().getX();

		int firstY = 0; // row
		int lastY = gameMap.getLastCoordinates().getY();
		
		int waterCountTop = 0;
		int waterCountBottom = 0;
		for (int x = firstX; x <= lastX; ++x) {
			Coordinates topTargetCoordinates = new Coordinates(x, firstY);
			Coordinates bottomTargetCoordinates = new Coordinates(x, lastY);
			
			if (gameMap.getTerrainAt(topTargetCoordinates) == ETerrain.Water) {
				++waterCountTop;
			}
			if (gameMap.getTerrainAt(bottomTargetCoordinates) == ETerrain.Water) {
				++waterCountBottom;
			}
		}
	
		int maxWaterOnSides = lastX/2;
		if (waterCountTop >= maxWaterOnSides || waterCountBottom >= maxWaterOnSides) {
			logger.debug("Water on the top side is: " + waterCountTop + " but should be <= " + maxWaterOnSides);
			logger.debug("Water on the bottom side is: " + waterCountBottom + " but should be <= " + maxWaterOnSides);
			return false;
		} else {
			return true;
		}
	}
	
	// Helper function for waterOnEdgesLeqHalf
	private boolean checkLeftAndRightForWater(ClientHalfMap gameMap) {
		
		int firstX = 0; // column
		int lastX = gameMap.getLastCoordinates().getX();

		int firstY = 0; // row
		int lastY = gameMap.getLastCoordinates().getY();
		
		int waterCountLeft = 0;
		int waterCountRight = 0;
		
		for (int y = firstY; y <= lastY; ++y) {
			Coordinates leftTargetCoordinates = new Coordinates(firstX, y);
			Coordinates rightTargetCoordinates = new Coordinates(lastX, y);
			
			if (gameMap.getTerrainAt(leftTargetCoordinates) == ETerrain.Water) {
				++waterCountLeft;
			}
			if (gameMap.getTerrainAt(rightTargetCoordinates) == ETerrain.Water) {
				++waterCountRight;
			}
		}
		int maxWaterOnSides = lastY/2;
		if (waterCountLeft >= maxWaterOnSides || waterCountRight >= lastY/2+1) {
			logger.debug("Water on the left side is: " + waterCountLeft + " but should be <= " + maxWaterOnSides);
			logger.debug("Water on the right side is: " + waterCountRight + " but should be <= " + maxWaterOnSides);
			return false;
		} else {
			return true;
		}
	}
	
	
	
	/*
	 * The idea is that we will create a bucket of checked grass/mountain (walkable)
	 * fields. The function will check the first walkable field and toss it into the
	 * bucket. After that it will be recursively called for all the walkable
	 * neighbours of the element that was last put in the bucket. This method is
	 * similar to Dijkstra method for pathfinding.
	 */

	private static boolean hasNoIslands(ClientHalfMap gameMap) {

		Set<Coordinates> visited = new LinkedHashSet<Coordinates>();

		// Start with a field that we are certain is grass and check every field
		// connected to it recursively
		checkNeighbours(gameMap.getMyStartingCoord(), visited, gameMap);

		// If there are walkable fields in the map that haven't been added to the set,
		// that mean that there
		// are islands on the map
		return fieldsCanBeVisited(gameMap, visited);

	}

	/**
	 * Helper function that compares the walkable fields in the gameMap to the ones
	 * in the "visited" set
	 */
	private static boolean fieldsCanBeVisited(ClientHalfMap gameMap, Set<Coordinates> visited) {
		// Get the dimensions of the map
		int lastX = gameMap.getLastCoordinates().getX();
		int lastY = gameMap.getLastCoordinates().getY();

		// Checks if every walkable field on the map visited (reachable)
		for (int Y = 0; Y <= lastY; ++Y) {
			for (int X = 0; X <= lastX; ++X) {
				Coordinates targetCoordinates = new Coordinates(X, Y);
				ETerrain targetTerrain = gameMap.getTerrainAt(targetCoordinates);
				// If the tile is walkable
				if (targetTerrain != ETerrain.Water) {
					// If its not found in the list of visited fields return false
					if (!visited.contains(targetCoordinates)) {
						return false;
					}
				}
			}
		}
		// Returns true by default if no unvisited nodes are found
		return true;
	}

	/**
	 * Helper function that recursively adds all connected walkable tiles to a list
	 */
	private static void checkNeighbours(Coordinates targetPos, Set<Coordinates> visited, ClientHalfMap gameMap) {

		// Check if the target node is already in the visited set
		if (!visited.contains(targetPos)) {
			// add the current node to the list
			visited.add(targetPos);

			ArrayList<Coordinates> surroundingPos = new ArrayList<Coordinates>();

			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Left));
			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Right));
			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Up));
			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Down));

			for (Coordinates c : surroundingPos) {
				
				if (c != null) {
				
					ETerrain targetTerrain = gameMap.getTerrainAt(c);

					if (targetTerrain != ETerrain.Water) {
						checkNeighbours(c, visited, gameMap);
					}
				}
			}
		}
	}	
}