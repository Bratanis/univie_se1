package client.model.pathfinder.helpers.appraiser;

import java.util.List;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;

public class TreasureSearchFieldAppraiser extends HalfMapSearchFieldAppraiser {

	public TreasureSearchFieldAppraiser(List<Coordinates> visitedCoordinates) {
		super(visitedCoordinates);
	}

	@Override
	protected boolean atGoal(MapNode mapNode) {
		return mapNode.hasTreasure();
	}

	@Override
	protected boolean nearGoal(MapNode mapNode) {
		return mapNode.isNearTreasure();
	}

}
