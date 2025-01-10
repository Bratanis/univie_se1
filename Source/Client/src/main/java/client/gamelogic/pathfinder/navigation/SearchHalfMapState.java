package client.gamelogic.pathfinder.navigation;

import java.util.Iterator;
import java.util.Map;

import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;

/**
 * 
 */
public abstract class SearchHalfMapState extends NavigationState {

	protected ETerritory targetTerritory = ETerritory.None;
	
	
	public SearchHalfMapState(PathFinderData currentData, ETerritory targetTerritory) {
		super(currentData);
		this.targetTerritory = targetTerritory;
	}
	
	@Override
	protected void removeOutOfBoundsFields(Map<Coordinates, MapNode> fields) {
		Iterator<Map.Entry<Coordinates, MapNode>> iterator = fields.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<Coordinates, MapNode> field = iterator.next();
	        if (!targetTerritory.contains(field.getKey())) { // If target teritory doesnt contain the coordinates of the field, remove it
	        	logger.debug("PathFinder remoing field " + field + " from considered fields: out of bounds for territory: " + targetTerritory);
	            iterator.remove(); 
	        }
	    } 
	}
	
	public void testRemoveOutOfBoundsFields(Map<Coordinates, MapNode> fields) {
		removeOutOfBoundsFields(fields);
	}
}