package client.gamelogic.pathfinder.navigation;

import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.helpers.appraiser.TreasureSearchFieldAppraiser;
import client.mvc.model.gamemap.mapelements.ETerritory;

public class SearchMyTerritoryForTreasure extends SearchHalfMapState{

	public SearchMyTerritoryForTreasure(PathFinderData currentData, ETerritory targetTErritory) {
		super(currentData, targetTErritory);
		this.appraiser = new TreasureSearchFieldAppraiser(visitedCoordinates);
	}

	@Override
	public boolean goalComplete() {
		return currentGameData.isTreasureCollected();
	}

}
