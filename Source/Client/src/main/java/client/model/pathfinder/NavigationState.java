package client.model.pathfinder;

import java.io.*;
import java.util.*;

import client.model.gamemap.mapelements.Coordinates;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public abstract class NavigationState {

	/**
	 * Default constructor
	 */
	public NavigationState() {
	}

	/**
	 * 
	 */
	private PathFinder pathFinderContext;

	/**
	 * 
	 */
	private Collection<Coordinates> visitedCoordinates;

	/**
	 * @param pathFinderContext
	 */
	public NavigationState(PathFinder pathFinderContext) {
		// TODO implement here
	}

	/**
	 * Will determine if it's time to switch to the next state (going to other side of the map
	 * if we are searching a half map or searching a half map if we are going to the other side) 
	 */
	public abstract void reevaluateCurrentState(); 
		
	
	/**
	 * @return
	 */
	public Queue<EMove> queueDeterminedNextMoves() {
		// TODO implement here
		return null;
	}

}