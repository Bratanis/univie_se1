package client.model.pathfinder;

import java.io.*;
import java.util.*;

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
	 * @return
	 */
	public Queue<EMove> queueDeterminedNextMoves() {
		// TODO implement here
		return null;
	}

}