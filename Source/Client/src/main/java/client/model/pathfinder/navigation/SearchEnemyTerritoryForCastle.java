package client.model.pathfinder.navigation;

import java.util.Map;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.ETerritory;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.appraiser.CastleSearchFieldAppraiser;

public class SearchEnemyTerritoryForCastle extends SearchHalfMapState {

	public SearchEnemyTerritoryForCastle(GameProgress gameProgress, ETerritory targetTErritory) {
		super(gameProgress, targetTErritory);
		this.appraiser = new CastleSearchFieldAppraiser(visitedCoordinates);
	}

	@Override
	public boolean goalComplete() {
		return gameProgress.gameIsOver();
	}

}
