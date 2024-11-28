package client.model.pathfinder;

import java.io.*;
import java.util.*;

import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class GoToEnemyTerritoryState extends NavigationState {

	/**
	 * Default constructor
	 */
	public GoToEnemyTerritoryState() {
	}

	/**
	 * 
	 */
	private EMove enemyDirection;

	/**
	 * 
	 */
	private boolean onHomeTerritory;

}