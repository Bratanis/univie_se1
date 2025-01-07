package client.gamelogic.pathfinder.navigation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.gamelogic.pathfinder.helpers.AppraisedMapField;
import client.gamelogic.pathfinder.helpers.OrientationHelper;
import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.helpers.appraiser.FieldAppraiser;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public abstract class NavigationState {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected List<Coordinates> visitedCoordinates;
	
	protected PathFinderData currentGameData;
	
	protected FieldAppraiser appraiser;
	protected OrientationHelper orientationHelper;

	/**
	 * Default constructor
	 */
	public NavigationState(PathFinderData data) {
		this.currentGameData = data;
		this.visitedCoordinates = new LinkedList<>();
		this.orientationHelper = new OrientationHelper();
	}


	/**
	 * Will be used to help determine if it's time to switch to the next state (going to other side of the map
	 * if we are searching a half map or searching a half map if we are going to the other side) 
	 * @return 
	 */
	public abstract boolean goalComplete(); 
		
	
	/**
	 * @return
	 */
	public Collection<EMove> determineNextMoves(Map<Coordinates, MapNode> surroundings){
		logger.debug("Adding my current position to visited coordinates: "
						+ visitedCoordinates.add(currentGameData.getMyPosition()));
		removeOutOfBoundsFields(surroundings);
		AppraisedMapField destinationNode = appraiser.getBestMapField(surroundings);
		return orientationHelper.getDirections(currentGameData.getMyPosition(), destinationNode);
	}
	
	protected abstract void removeOutOfBoundsFields (Map<Coordinates, MapNode> fields);

}






