package client.gamelogic.pathfinder.helpers.appraiser;

import java.util.Collection;

import client.gamelogic.pathfinder.helpers.AppraisedMapField;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public abstract class HalfMapSearchFieldAppraiser extends FieldAppraiser {

	public HalfMapSearchFieldAppraiser(Collection<Coordinates> visitedCoordinates) {
		super(visitedCoordinates);
	}


	@Override
	protected AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode) {
		
		ENodePriority priority = ENodePriority.Forbidden;
		
		if (atGoal(mapNode))
			priority = ENodePriority.VeryHigh;
		
		else if (nearGoal(mapNode) && mapNode.getTerrain() != ETerrain.Water)
			priority = ENodePriority.High;
		
		else if (!visitedCoordinates.contains(coordinates)) {
			
			if (mapNode.getTerrain() == ETerrain.Mountain)	// We prefer mountains over grass for unvisited fields
				priority = ENodePriority.Medium;
			else if (mapNode.getTerrain() == ETerrain.Grass)// Grass is still better than an already visited field
				priority = ENodePriority.Low;
			
		} else if ((visitedCoordinates.contains(coordinates)) ){
				priority = ENodePriority.VeryLow;			// visited fields are not very nice but still allowed
		} else {
			assert (mapNode.getTerrain() == ETerrain.Water);
		}
		
		return new AppraisedMapField(coordinates, mapNode, priority);
	}
	
	
	protected abstract boolean atGoal(MapNode mapNode);
	protected abstract boolean nearGoal(MapNode mapNode);
	
	
		
	
}