package client.gamelogic.mapcreation.validation.rules;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

/*
 * The idea is that we will create a bucket of checked grass/mountain (walkable)
 * fields. The function will check the first walkable field and toss it into the
 * bucket. After that it will be recursively called for all the walkable
 * neighbours of the element that was last put in the bucket. 
 */
public class NoIslandsRule implements ValidationRule{

	@Override
	public void validate(ClientHalfMap testMap, NotificationCollector collector) {
		Set<Coordinates> visited = new LinkedHashSet<Coordinates>();

		// Start with a field that we are certain is grass and check every field
		// connected to it recursively
		checkNeighbours(getSomeGrassNode(testMap), visited, testMap);

		// If there are walkable fields in the map that haven't been added to the set,
		// that mean that there
		// are islands on the map
		checkIfAllFieldsCanBeVisited(testMap, visited, collector);
	}
	
	
	private Coordinates getSomeGrassNode(ClientHalfMap testMap){
		int testX = 0;
		int testY = 0;
		Coordinates testCoordinates = new Coordinates (testX, testY);
		while (testMap.getTerrainAt(testCoordinates) != ETerrain.Grass) {
			if (testX < ClientHalfMap.LAST_COORDINATES.getX())
				testCoordinates = new Coordinates (testX++, testY);
			else if (testY < ClientHalfMap.LAST_COORDINATES.getY())
				testCoordinates = new Coordinates (testX, testY++);
			else throw new IllegalStateException ("Map validator couldn't find any grass nodes on the map!");
		}
		return testCoordinates;
	}

	/**
	 * Helper function that compares the walkable fields in the gameMap to the ones
	 * in the "visited" set
	 */
	private void checkIfAllFieldsCanBeVisited(ClientHalfMap gameMap, Set<Coordinates> visited, NotificationCollector collector) {
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
						collector.addNotification("Found a field that cannot be visited! The map has islands!");
						return;
					}
				}
			}
		}
	}

	/**
	 * Helper function that recursively adds all connected walkable tiles to a list
	 */
	private void checkNeighbours(Coordinates targetPos, Set<Coordinates> visited, ClientHalfMap gameMap) {

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
