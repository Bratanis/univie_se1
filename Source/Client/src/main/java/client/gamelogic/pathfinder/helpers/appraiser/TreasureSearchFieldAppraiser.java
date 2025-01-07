package client.gamelogic.pathfinder.helpers.appraiser;

import java.util.Collection;

import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

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
