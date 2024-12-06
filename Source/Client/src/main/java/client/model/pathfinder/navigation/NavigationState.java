package client.model.pathfinder.navigation;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.AppraisedMapField;
import client.model.pathfinder.helpers.OrientationHelper;
import client.model.pathfinder.helpers.appraiser.FieldAppraiser;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public abstract class NavigationState {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected List<Coordinates> visitedCoordinates;
	
	protected GameProgress gameProgress;
	
	protected FieldAppraiser appraiser;
	protected OrientationHelper orientationHelper;

	/**
	 * Default constructor
	 */
	public NavigationState(GameProgress gameProgress) {
		this.gameProgress = gameProgress;
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
						+ visitedCoordinates.add(gameProgress.getCurrentCoordinates()));
		removeOutOfBoundsFields(surroundings);
		AppraisedMapField destinationNode = appraiser.getBestMapField(surroundings);
		return orientationHelper.getDirections(gameProgress.getCurrentCoordinates(), destinationNode);
	}
	
	protected abstract void removeOutOfBoundsFields (Map<Coordinates, MapNode> fields);

}






