package client.model.pathfinder;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.ETerritory;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.navigation.GoToEnemyTerritoryState;
import client.model.pathfinder.navigation.NavigationState;
import client.model.pathfinder.navigation.SearchEnemyTerritoryForCastle;
import client.model.pathfinder.navigation.SearchMyTerritoryForTreasure;
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
	private boolean isDefined = false;
	private NavigationState currentNavigationState;
	private List<NavigationState> unusedStates;
	private Queue<EMove> queuedMoves;
	
	private PathFinder() {
		
	}
	
	public static PathFinder getUndifinedInstance() {
		return new PathFinder();
	}
	
	public boolean isDefined() {
		return this.isDefined;
	}

	/**
	 * @param gameProgress GameProgress
	 */
	public PathFinder(GameProgress gameProgress, ETerritory myTerritory, ETerritory enemyTerritory) {
		
		this.queuedMoves = new LinkedList<>();
		
		this.unusedStates = new ArrayList<>();

		NavigationState searchMyTerritoryState = new SearchMyTerritoryForTreasure(gameProgress, myTerritory);
		unusedStates.add(searchMyTerritoryState);
		NavigationState goToEnemyTerritoryState = new GoToEnemyTerritoryState(gameProgress, myTerritory, enemyTerritory);
		unusedStates.add(goToEnemyTerritoryState);
		NavigationState searchEnemyTerritoryState = new SearchEnemyTerritoryForCastle(gameProgress, enemyTerritory);
		unusedStates.add(searchEnemyTerritoryState);

		this.currentNavigationState = unusedStates.removeFirst();
		isDefined = true;
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
	
	private void reevaluateCurrentState() throws NoSuchElementException{
		if (currentNavigationState.goalComplete()) {
			this.currentNavigationState = unusedStates.removeFirst();
	
		}
	}
}