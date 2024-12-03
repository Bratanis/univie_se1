package client.model.pathfinder.navigation;

import java.io.*;
import java.util.*;

import client.customexceptions.MapNavigationException;
import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.ETerritory;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.appraiser.EnemyDirFieldAppraiser;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class GoToEnemyTerritoryState extends NavigationState {

	private final ETerritory enemyTerritory; // used to determine when enemy territory is reached!

	public GoToEnemyTerritoryState(GameProgress gameProgress, ETerritory myTerritory, ETerritory enemyTerritory) {
		super(gameProgress);
		this.enemyTerritory = enemyTerritory;
		EMove enemyDirection = determineEnemyDir(myTerritory, enemyTerritory);
		this.appraiser = new EnemyDirFieldAppraiser (visitedCoordinates, enemyDirection);
	}

	private EMove determineEnemyDir(ETerritory myTerritory, ETerritory enemyTerritory) {
	       // Determine direction
        if (myTerritory == ETerritory.TopLeftSide) {
            if (enemyTerritory == ETerritory.BottomSide) {
                return EMove.Down;
            } else if (enemyTerritory == ETerritory.RightSide) {
            	return EMove.Right;
            }
        } else if (enemyTerritory == ETerritory.TopLeftSide) {
            if (myTerritory == ETerritory.BottomSide) {
            	return EMove.Up;
            } else if (myTerritory == ETerritory.RightSide) {
            	return EMove.Left;
            }
        } 
        	throw new MapNavigationException("Unexpected territory configuration: " 
        										+ myTerritory + "; "
        										+ enemyTerritory);
	}

	@Override
	public boolean goalComplete() {
		
		return enemyTerritory.contains(gameProgress.getCurrentCoordinates());
	}

// move this method to SearchHalfMapState to respect Liskov!
	@Override
	protected void removeOutOfBoundsFields(Map<Coordinates, MapNode> fields) {
		// TODO Auto-generated method stub
		
	}

}