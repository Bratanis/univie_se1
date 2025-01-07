package client.gamelogic.pathfinder.helpers;

import java.util.Map;

import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

/**
 * This class gets fed into the PathFinder to help it determine the next move.
 * It is data coming from the server and is not used in the mvc model
 */
public class PathFinderData {

	private Coordinates myPosition;
	private Map<Coordinates, MapNode> surroundings;
	private boolean treasureCollected;
	
	public PathFinderData(Coordinates myPosition, Map<Coordinates, MapNode> surroundings, boolean treasureCollected) {
		super();
		this.myPosition = myPosition;
		this.surroundings = surroundings;
		this.treasureCollected = treasureCollected;
	}

	public Coordinates getMyPosition() {
		return myPosition;
	}

	public void setMyPosition(Coordinates myPosition) {
		this.myPosition = myPosition;
	}

	public Map<Coordinates, MapNode> getSurroundings() {
		return surroundings;
	}

	public void setSurroundings(Map<Coordinates, MapNode> surroundings) {
		this.surroundings = surroundings;
	}

	public boolean isTreasureCollected() {
		return treasureCollected;
	}

	public void setTreasureCollected(boolean treasureCollected) {
		this.treasureCollected = treasureCollected;
	}
	
	
	
	
}
