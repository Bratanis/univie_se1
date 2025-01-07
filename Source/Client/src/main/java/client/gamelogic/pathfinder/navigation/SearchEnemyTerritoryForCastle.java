package client.gamelogic.pathfinder.navigation;

import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.helpers.appraiser.CastleSearchFieldAppraiser;
import client.mvc.model.gamemap.mapelements.ETerritory;

public class SearchEnemyTerritoryForCastle extends SearchHalfMapState {

	public SearchEnemyTerritoryForCastle(PathFinderData currentData, ETerritory targetTErritory) {
		super(currentData, targetTErritory);
		this.appraiser = new CastleSearchFieldAppraiser(visitedCoordinates);
	}

	@Override
	public boolean goalComplete() {
		return false; // This is the final stage of the pathFinder and the game gets terminated after the castle is reached
	}

	

}
