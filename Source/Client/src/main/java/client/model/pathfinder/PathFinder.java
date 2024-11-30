package client.model.pathfinder;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;

/**
 * Using State pattern here is more appropriate than Strategy, because the transitions from one state to the other
 * are deterministic, and the different states accomplish different goals.
 */
public class PathFinder {


	/**
	 * Attributes
	 */
	private NavigationState currentNavigationState;
	private List<NavigationState> unusedStates;
	private Queue<EMove> queuedMoves;

	/**
	 * @param gameProgress GameProgress
	 */
	public PathFinder(GameProgress gameProgress) {
		
		this.queuedMoves = new LinkedList<>();
		
		this.unusedStates = new ArrayList<>();

		NavigationState searchMyTerritoryState = new SearchHalfMapState(gameProgress);
		unusedStates.add(searchMyTerritoryState);
		NavigationState goToEnemyTerritoryState = new GoToEnemyTerritoryState(gameProgress);
		unusedStates.add(goToEnemyTerritoryState);
		NavigationState searchEnemyTerritoryState = new SearchHalfMapState(gameProgress);
		unusedStates.add(searchEnemyTerritoryState);

		this.currentNavigationState = unusedStates.removeFirst();
	}
	
	public boolean noPendingMoves() {
		return queuedMoves.isEmpty();
	}



	/**
	 * @param surroundings 
	 * @param currentCoord
	 */
	public void loadNextMoves(Map<Coordinates, MapNode> surroundings, Coordinates currentCoord) {
		reevaluateCurrentState();
		Collection <EMove> naviagtionMoves = currentNavigationState.determineNextMoves(surroundings);
		this.queuedMoves.addAll(naviagtionMoves);
	}
	
	public EMove getNextMove() {
		return queuedMoves.remove();
	}
	
	private void reevaluateCurrentState() {
		if (currentNavigationState.goalComplete())
			this.currentNavigationState = unusedStates.removeFirst();
	}
}