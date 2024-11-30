package client.model.pathfinder;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public abstract class NavigationState {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected Collection<Coordinates> visitedCoordinates;
	
	protected GameProgress gameProgress;

	/**
	 * Default constructor
	 */
	public NavigationState(GameProgress gameProgress) {
		this.gameProgress = gameProgress;
		this.visitedCoordinates = new ArrayList<>();
	}

	//private PathFinder pathFinderContext;

	//public NavigationState(PathFinder pathFinderContext) {
		// TODO implement here
	//}

	/**
	 * Will be used to help determine if it's time to switch to the next state (going to other side of the map
	 * if we are searching a half map or searching a half map if we are going to the other side) 
	 * @return 
	 */
	public abstract boolean goalComplete(); 
		
	
	/**
	 * @return
	 */
	public abstract Collection<EMove> determineNextMoves(Map<Coordinates, MapNode> surroundings); 

}






