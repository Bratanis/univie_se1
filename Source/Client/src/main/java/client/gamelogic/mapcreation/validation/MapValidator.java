package client.gamelogic.mapcreation.validation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.gamelogic.mapcreation.validation.rules.MapNotNullRule;
import client.gamelogic.mapcreation.validation.rules.MaxWaterOnEdgesRule;
import client.gamelogic.mapcreation.validation.rules.NoIslandsRule;
import client.gamelogic.mapcreation.validation.rules.TerrainCountRule;
import client.gamelogic.mapcreation.validation.rules.ValidationRule;
import client.mvc.model.gamemap.ClientHalfMap;

/**
 * 
 */
public class MapValidator {
	
	private final Logger logger;
	private final List<ValidationRule> rules = new ArrayList<>();
    private final NotificationCollector collector;
	

	/**
	 * Default constructor
	 */
	public MapValidator(NotificationCollector collector) {
		this.logger = LoggerFactory.getLogger(MapValidator.class);
		this.collector = collector;
		 rules.add(new MapNotNullRule());
		 rules.add(new TerrainCountRule());
	     rules.add(new MaxWaterOnEdgesRule());
	     rules.add(new NoIslandsRule());
	}

	/**
	 * @param testMap 
	 * @return
	 */
	public boolean mapIsValid(ClientHalfMap testMap) {
		
		// Make sure we are starting with a blank slate
		collector.clear();

        for (ValidationRule rule : rules) {
            rule.validate(testMap, collector);
        }

        if (collector.getNotifications().isEmpty()) {
            logger.info("Map is valid.");
            return true;
        } else {
            logger.info("Map is invalid:");
            collector.getNotifications().forEach(logger::info);
            return false;
        }
	}
	
	

//	private void checkIfMinNumOfEachTerrainPresent(ClientHalfMap testMap) {
//	
//		int grassCount = 0;
//		int mountainCount = 0;
//		int waterCount = 0;
//		int totalFieldCount = 0;
//		boolean castlePresent = false;
//		
//		int lastX = testMap.getLastCoordinates().getX();
//	    int lastY = testMap.getLastCoordinates().getY();
//
//	    for (int y = 0; y <= lastY; y++) {
//	        for (int x = 0; x <= lastX; x++) {
//	        	
//	        	++totalFieldCount;
//	            
//	        	Coordinates coordinates = new Coordinates(x, y);
//	            ETerrain terrain = testMap.getTerrainAt(coordinates);
//
//	            switch (terrain) {
//	                case Grass:
//	                    ++grassCount;
//	                    break;
//	                case Mountain:
//	                    ++mountainCount;
//	                    break;
//	                case Water:
//	                    ++waterCount;
//	                    break;
//	            }
//
//	            // Check for castle presence
//	            if (testMap.hasCastleAt(coordinates)) {
//	                castlePresent = true;
//	            }
//	        }
//	    }
//
//	    // Check minimum terrain counts
//	    if (grassCount < MIN_GRASS_FIELDS) {
//	        notifications.add("Map has fewer grass fields than the minimum required (" + MIN_GRASS_FIELDS + ").");
//	    }
//	    if (mountainCount < MIN_MOUNTAIN_FIELDS) {
//	        notifications.add("Map has fewer mountain fields than the minimum required (" + MIN_MOUNTAIN_FIELDS + ").");
//	    }
//	    if (waterCount < MIN_WATER_FIELDS) {
//	        notifications.add("Map has fewer water fields than the minimum required (" + MIN_WATER_FIELDS + ").");
//	    }
//	    if (totalFieldCount != TOTAL_NUM_OF_FIELDS) {
//	    	notifications.add("Map should have " + TOTAL_NUM_OF_FIELDS + " but has " + totalFieldCount);
//	    }
//	    if (!castlePresent) {
//	        notifications.add("No castle is present on the map.");
//	    }	
//		
//	}

	/**
	 * Map can have max 2 water on the short sides and max 4 water on the long sides
	 * @param gameMap
	 * @return
	 */
//	private void checkIfWaterOnEdgesLeqHalf(ClientHalfMap gameMap) {
//		
//		// Opposite sides are checked at the same time to reduce the number of times the map needs to be checked
//		// (opposide side water fields can be counted in the same loop)
//		checkTopAndBottomForWater(gameMap);
//		checkLeftAndRightForWater(gameMap);
//	}
//
//
//	// Helper function for waterOnEdgesLeqHalf
//	private void checkTopAndBottomForWater(ClientHalfMap gameMap) {
//		
//		int firstX = 0; // column
//		int lastX = gameMap.getLastCoordinates().getX();
//
//		int firstY = 0; // row
//		int lastY = gameMap.getLastCoordinates().getY();
//		
//		int waterCountTop = 0;
//		int waterCountBottom = 0;
//		for (int x = firstX; x <= lastX; ++x) {
//			Coordinates topTargetCoordinates = new Coordinates(x, firstY);
//			Coordinates bottomTargetCoordinates = new Coordinates(x, lastY);
//			
//			if (gameMap.getTerrainAt(topTargetCoordinates) == ETerrain.Water) {
//				++waterCountTop;
//			}
//			if (gameMap.getTerrainAt(bottomTargetCoordinates) == ETerrain.Water) {
//				++waterCountBottom;
//			}
//		}
//	
//		int maxWaterOnSides = lastX/2;
//		if (waterCountTop > maxWaterOnSides) {
//			notifications.add("Water on the top side is: " + waterCountTop + " but should be <= " + maxWaterOnSides);
//		}
//		if (waterCountBottom > maxWaterOnSides){
//			notifications.add("Water on the bottom side is: " + waterCountBottom + " but should be <= " + maxWaterOnSides);
//		}
//	}

	
	// Helper function for waterOnEdgesLeqHalf
//	private void checkLeftAndRightForWater(ClientHalfMap gameMap) {
//		
//		int firstX = 0; // column
//		int lastX = gameMap.getLastCoordinates().getX();
//
//		int firstY = 0; // row
//		int lastY = gameMap.getLastCoordinates().getY();
//		
//		int waterCountLeft = 0;
//		int waterCountRight = 0;
//		
//		for (int y = firstY; y <= lastY; ++y) {
//			Coordinates leftTargetCoordinates = new Coordinates(firstX, y);
//			Coordinates rightTargetCoordinates = new Coordinates(lastX, y);
//			
//			if (gameMap.getTerrainAt(leftTargetCoordinates) == ETerrain.Water) {
//				++waterCountLeft;
//			}
//			if (gameMap.getTerrainAt(rightTargetCoordinates) == ETerrain.Water) {
//				++waterCountRight;
//			}
//		}
//		int maxWaterOnSides = lastY/2;
//		if (waterCountLeft > maxWaterOnSides ) {
//			notifications.add("Water on the left side is: " + waterCountLeft + " but should be <= " + maxWaterOnSides);
//		}
//		if (waterCountRight > maxWaterOnSides) {
//			notifications.add("Water on the right side is: " + waterCountRight + " but should be <= " + maxWaterOnSides);
//		}
//	}
	
	
	
	/*
	 * The idea is that we will create a bucket of checked grass/mountain (walkable)
	 * fields. The function will check the first walkable field and toss it into the
	 * bucket. After that it will be recursively called for all the walkable
	 * neighbours of the element that was last put in the bucket. 
	 */

//	private void checkForIslands(ClientHalfMap gameMap) {
//
//		Set<Coordinates> visited = new LinkedHashSet<Coordinates>();
//
//		// Start with a field that we are certain is grass and check every field
//		// connected to it recursively
//		checkNeighbours(getSomeGrassNode(gameMap), visited, gameMap);
//
//		// If there are walkable fields in the map that haven't been added to the set,
//		// that mean that there
//		// are islands on the map
//		checkIfAllFieldsCanBeVisited(gameMap, visited);
//
//	}
	
//	private Coordinates getSomeGrassNode(ClientHalfMap testMap){
//		int testX = 0;
//		int testY = 0;
//		Coordinates testCoordinates = new Coordinates (testX, testY);
//		while (testMap.getTerrainAt(testCoordinates) != ETerrain.Grass) {
//			if (testX < ClientHalfMap.LAST_COORDINATES.getX())
//				testCoordinates = new Coordinates (testX++, testY);
//			else if (testY < ClientHalfMap.LAST_COORDINATES.getY())
//				testCoordinates = new Coordinates (testX, testY++);
//			else throw new IllegalStateException ("Map validator couldn't find any grass nodes on the map!");
//		}
//		return testCoordinates;
//	}
//
//	/**
//	 * Helper function that compares the walkable fields in the gameMap to the ones
//	 * in the "visited" set
//	 */
//	private void checkIfAllFieldsCanBeVisited(ClientHalfMap gameMap, Set<Coordinates> visited) {
//		// Get the dimensions of the map
//		int lastX = gameMap.getLastCoordinates().getX();
//		int lastY = gameMap.getLastCoordinates().getY();
//
//		// Checks if every walkable field on the map visited (reachable)
//		for (int Y = 0; Y <= lastY; ++Y) {
//			for (int X = 0; X <= lastX; ++X) {
//				Coordinates targetCoordinates = new Coordinates(X, Y);
//				ETerrain targetTerrain = gameMap.getTerrainAt(targetCoordinates);
//				// If the tile is walkable
//				if (targetTerrain != ETerrain.Water) {
//					// If its not found in the list of visited fields return false
//					if (!visited.contains(targetCoordinates)) {
//						notifications.add("Found a field that cannot be visited! The map has islands!");
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * Helper function that recursively adds all connected walkable tiles to a list
//	 */
//	private void checkNeighbours(Coordinates targetPos, Set<Coordinates> visited, ClientHalfMap gameMap) {
//
//		// Check if the target node is already in the visited set
//		if (!visited.contains(targetPos)) {
//			// add the current node to the list
//			visited.add(targetPos);
//
//			ArrayList<Coordinates> surroundingPos = new ArrayList<Coordinates>();
//
//			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Left));
//			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Right));
//			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Up));
//			surroundingPos.add(targetPos.getCoordinatesToThe(EMove.Down));
//
//			for (Coordinates c : surroundingPos) {
//				
//				if (c != null) {
//				
//					ETerrain targetTerrain = gameMap.getTerrainAt(c);
//
//					if (targetTerrain != ETerrain.Water) {
//						checkNeighbours(c, visited, gameMap);
//					}
//				}
//			}
//		}
//	}	
}