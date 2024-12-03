package client.model.pathfinder.helpers.appraiser;

import java.util.Collection;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.AppraisedMapField;

public class TreasureSearchFieldAppraiser extends HalfMapSearchFieldAppraiser{

	public TreasureSearchFieldAppraiser(Collection<Coordinates> visitedCoordinates) {
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
