package client.gamelogic.pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.navigation.GoToEnemyTerritoryState;
import client.gamelogic.pathfinder.navigation.NavigationState;
import client.gamelogic.pathfinder.navigation.SearchEnemyTerritoryForCastle;
import client.gamelogic.pathfinder.navigation.SearchMyTerritoryForTreasure;
import messagesbase.messagesfromclient.EMove;

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

	private PathFinderData currentData;
	
	private PathFinder() {
		
	}
	
	public NavigationState getCurrentNavigationState() {
		return currentNavigationState;
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
	// Future version must determine territories without the help of the map!!!
	public PathFinder(PathFinderData data, MapTerritories territories) {
		
		this.currentData = data;
		
		this.queuedMoves = new LinkedList<>();
		
		this.unusedStates = new ArrayList<>();

		NavigationState searchMyTerritoryState = new SearchMyTerritoryForTreasure(data, territories.getMyTerritory());
		unusedStates.add(searchMyTerritoryState);
		NavigationState goToEnemyTerritoryState = new GoToEnemyTerritoryState(data, territories.getMyTerritory(), territories.getEnemyTerriotry());
		unusedStates.add(goToEnemyTerritoryState);
		NavigationState searchEnemyTerritoryState = new SearchEnemyTerritoryForCastle(data, territories.getEnemyTerriotry());
		unusedStates.add(searchEnemyTerritoryState);

		this.currentNavigationState = unusedStates.removeFirst();
		isDefined = true;
	}
	
	
	
	
//public boolean noPendingMoves() {
//		return queuedMoves.isEmpty();
//	}
	
	public void updateData (PathFinderData newData) {
		currentData.setMyPosition(newData.getMyPosition());
		currentData.setSurroundings(newData.getSurroundings());
		currentData.setTreasureCollected(newData.isTreasureCollected());
	}



	/**
	 * @param surroundings 
	 * @param currentCoord
	 */
	private void loadNextMoves() {
		reevaluateCurrentState();
		Collection <EMove> naviagtionMoves = currentNavigationState.determineNextMoves(currentData.getSurroundings());
		this.queuedMoves.addAll(naviagtionMoves);
	}
	
	public EMove getNextMove() {

		assert (isDefined);
		if (queuedMoves.isEmpty()) {
			loadNextMoves();
		}
		return queuedMoves.remove();
	}
	
	private void reevaluateCurrentState() throws NoSuchElementException{
		if (currentNavigationState.goalComplete()) {
			this.currentNavigationState = unusedStates.removeFirst();
	
		}
	}
}