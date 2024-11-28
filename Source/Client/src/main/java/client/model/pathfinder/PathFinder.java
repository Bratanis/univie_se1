package client.model.pathfinder;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class PathFinder {


	/**
	 * Attributes
	 */
	private NavigationState currentNavigationState ;
	private NavigationState searchMyTerritoryState;
	private NavigationState goToEnemyTerritoryState;
	private NavigationState searchEnemyTerritoryState;
	private Queue<EMove> movesQueue;
	private PropertyChangeListener gameProgressListener;

	/**
	 * @param gameProgress GameProgress
	 */
	public PathFinder(GameProgress gameProgress) {
		this.searchMyTerritoryState = new SearchHalfMapState();
		this.goToEnemyTerritoryState = new GoToEnemyTerritoryState();
		this.searchEnemyTerritoryState = new SearchHalfMapState();
		this.currentNavigationState = searchMyTerritoryState;
		this.movesQueue = new LinkedList();
	}

	/**
	 * @return
	 */
	public EMove getNextMove() {
		// TODO implement here
		return null;
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
		currentNavigationState.reevaluateCurrentState();
	}
	
	public void nextNavigationState() {
	
	}
	}

}