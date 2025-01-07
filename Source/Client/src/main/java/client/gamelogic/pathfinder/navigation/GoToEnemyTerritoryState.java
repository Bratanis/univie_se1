package client.gamelogic.pathfinder.navigation;

import java.util.Map;

import client.customexceptions.MapNavigationException;
import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.helpers.appraiser.EnemyDirFieldAppraiser;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class GoToEnemyTerritoryState extends NavigationState {

	private final ETerritory enemyTerritory; // used to determine when enemy territory is reached!

	public GoToEnemyTerritoryState(PathFinderData currentData, ETerritory myTerritory, ETerritory enemyTerritory) {
		super(currentData);
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
		
		return enemyTerritory.contains(currentGameData.getMyPosition());
	}

// move this method to SearchHalfMapState to respect Liskov!
	@Override
	protected void removeOutOfBoundsFields(Map<Coordinates, MapNode> fields) {
		// TODO Auto-generated method stub
		
	}

}