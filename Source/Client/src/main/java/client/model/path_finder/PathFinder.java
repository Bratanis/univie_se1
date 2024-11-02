package Model.PathFinder;

import Model.GameMap.Coordinates;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class PathFinder {

	/**
	 * Default constructor
	 */
	public PathFinder() {
	}

	/**
	 * 
	 */
	private NavigationState currentNavigationState = searchMyTerritoryState;

	/**
	 * 
	 */
	private NavigationState searchMyTerritoryState;

	/**
	 * 
	 */
	private NavigationState goToEnemyTerritoryState;

	/**
	 * 
	 */
	private NavigationState searchEnemyTerritoryState;

	/**
	 * 
	 */
	private Queue<EMove> movesQueue;

	/**
	 * 
	 */
	private PropertyChangeListener gameProgressListener;

	/**
	 * @param gameProgress GameProgress
	 */
	public PathFinder(void gameProgress GameProgress) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public EMove getNextMove() {
		// TODO implement here
		return null;
	}

	/**
	 * 
	 */
	private void reevaluateGameState() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public boolean noPendingMoves() {
		// TODO implement here
		return false;
	}

	/**
	 * @param surroundings 
	 * @param currentCoord
	 */
	public void determineNextMoves(HashMap<Coordinates, MapNode> surroundings, Coordinates currentCoord) {
		// TODO implement here
	}

}