package client.model.pathfinder.helpers;

import java.util.ArrayList;
import java.util.Collection;

import client.customexceptions.MapNavigationException;
import client.model.gamemap.mapelements.Coordinates;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

public class OrientationHelper {

	int numOfMovesToLeaveCurrentField = 1; // By default to leave the starting position takes 1 move (grass field)
	
	public Collection<EMove> getDirections(Coordinates currentCoordinates, AppraisedMapField destinationField) {

		Collection <EMove> movesToDestination = new ArrayList<>();

		int numOfMovesToEnterOrLeaveDestination = howManyMovesToEnterOrLeave(destinationField);
		
		int totalNumOfMovesToReachDestination = numOfMovesToLeaveCurrentField + numOfMovesToEnterOrLeaveDestination;

		Coordinates destinationCoordinates = destinationField.getCoordinates();
		EMove direction = currentCoordinates.directionTo(destinationCoordinates);
		
		for (int moveCount = 0; moveCount < totalNumOfMovesToReachDestination; ++moveCount) {
			movesToDestination.add(direction);
		}
		
		// The field we enter after completing all of the queued moves is our new current field!
		numOfMovesToLeaveCurrentField = numOfMovesToEnterOrLeaveDestination;
		
		return movesToDestination;
	}
	
	private int howManyMovesToEnterOrLeave(AppraisedMapField destination) {
		
		ETerrain destinationTerrain = destination.getTerrain();
		if (destinationTerrain.equals(ETerrain.Mountain))
			return 2;
		else if (destinationTerrain.equals(ETerrain.Grass))
			return 1;
		else throw new MapNavigationException("The destination field has to be either grass or mountain, "
				+ "but instead is: " + destinationTerrain);
	}

}
