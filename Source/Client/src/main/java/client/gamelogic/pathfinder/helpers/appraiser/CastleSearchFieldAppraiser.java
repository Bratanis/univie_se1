package client.gamelogic.pathfinder.helpers.appraiser;

import java.util.Collection;

import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

public class CastleSearchFieldAppraiser extends HalfMapSearchFieldAppraiser{

	public CastleSearchFieldAppraiser(Collection<Coordinates> visitedCoordinates) {
		super(visitedCoordinates);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean atGoal(MapNode mapNode) {
		return mapNode.hasCastle();
	}

	@Override
	public boolean nearGoal(MapNode mapNode) {
		return mapNode.isNearCastle();
	}

}
