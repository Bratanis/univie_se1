package client.model.pathfinder.navigation;

import client.model.GameProgress;
import client.model.gamemap.mapelements.ETerritory;
import client.model.pathfinder.helpers.appraiser.TreasureSearchFieldAppraiser;

public class SearchMyTerritoryForTreasure extends SearchHalfMapState{

	public SearchMyTerritoryForTreasure(GameProgress gameProgress, ETerritory targetTErritory) {
		super(gameProgress, targetTErritory);
		this.appraiser = new TreasureSearchFieldAppraiser(visitedCoordinates);
	}

	@Override
	public boolean goalComplete() {
		return gameProgress.treasureCollected();
	}

}
