package client.model.pathfinder.helpers.appraiser;

import java.util.List;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;

public class CastleSearchFieldAppraiser extends HalfMapSearchFieldAppraiser {

	public CastleSearchFieldAppraiser(List<Coordinates> visitedCoordinates) {
		super(visitedCoordinates);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean atGoal(MapNode mapNode) {
		return mapNode.hasCastle();
	}

	@Override
	protected boolean nearGoal(MapNode mapNode) {
		return mapNode.isNearCastle();
	}

}
