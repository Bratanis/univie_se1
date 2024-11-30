package client.model.pathfinder;

import java.io.*;
import java.util.*;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class GoToEnemyTerritoryState extends NavigationState {


	public GoToEnemyTerritoryState(GameProgress gameProgress) {
		super(gameProgress);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private EMove enemyDirection;

	/**
	 * 
	 */
	private boolean onHomeTerritory;

	@Override
	public boolean goalComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<EMove> determineNextMoves(Map<Coordinates, MapNode> surroundings) {
		// TODO Auto-generated method stub
		return null;
	}

}