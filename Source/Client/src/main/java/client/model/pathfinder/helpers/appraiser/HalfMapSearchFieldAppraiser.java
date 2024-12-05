package client.model.pathfinder.helpers.appraiser;

import java.util.List;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.AppraisedMapField;
import messagesbase.messagesfromclient.ETerrain;

public abstract class HalfMapSearchFieldAppraiser extends FieldAppraiser {

	public HalfMapSearchFieldAppraiser(List<Coordinates> visitedCoordinates) {
		super(visitedCoordinates);
	}

//	@Override
//	protected AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode) {
//
//		ENodePriority priority = ENodePriority.Forbidden;
//
//		if (atGoal(mapNode))
//			priority = ENodePriority.VeryHigh;
//
//		else if (nearGoal(mapNode))
//			priority = ENodePriority.High;
//
//		else if (!visitedCoordinates.contains(coordinates)) {
//
//			if (mapNode.getTerrain() == ETerrain.Mountain) // We prefer mountains over grass for unvisited fields
//				priority = ENodePriority.Medium;
//			else if (mapNode.getTerrain() == ETerrain.Grass)// Grass is still better than an already visited field
//				priority = ENodePriority.Low;
//
//		} else if ((visitedCoordinates.contains(coordinates))) {
//
//			priority = ENodePriority.VeryLow; // visited fields are not very nice but still allowed
//		} else {
//			assert (mapNode.getTerrain() == ETerrain.Water);
//		}
//
//		return new AppraisedMapField(coordinates, mapNode, priority);
//	}

	@Override
	protected AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode) {

		Integer desirabilityIndex = Integer.MIN_VALUE; // undesirable by default

		if (atGoal(mapNode))
			desirabilityIndex = Integer.MAX_VALUE;

		else if (nearGoal(mapNode))
			desirabilityIndex = Integer.MAX_VALUE - 1;

		else if (!visitedCoordinates.contains(coordinates)) {

			if (mapNode.getTerrain() == ETerrain.Mountain) // We prefer mountains over grass for unvisited fields
				desirabilityIndex = Integer.MAX_VALUE - 2;
			else if (mapNode.getTerrain() == ETerrain.Grass)// Grass is still better than an already visited field
				desirabilityIndex = Integer.MAX_VALUE - 3;

		} else if ((visitedCoordinates.contains(coordinates))) {
			int recencyIndex = visitedCoordinates.indexOf(coordinates);
			// More recently visited fields get lower priority
			desirabilityIndex = -recencyIndex;
		} else {
			assert (mapNode.getTerrain() == ETerrain.Water);
		}

		return new AppraisedMapField(coordinates, mapNode, desirabilityIndex);
	}

	protected abstract boolean atGoal(MapNode mapNode);

	protected abstract boolean nearGoal(MapNode mapNode);

}