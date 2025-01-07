package client.mvc.model.gamemap.mapelements;

import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.LongGameMap;
import client.mvc.model.gamemap.SquareGameMap;

public enum ETerritory {
	
	TopLeftSide(new Coordinates(), ClientHalfMap.LAST_COORDINATES),
	BottomSide(ClientHalfMap.LAST_COORDINATES, SquareGameMap.LAST_COORDINATES),
	RightSide(ClientHalfMap.LAST_COORDINATES, LongGameMap.LAST_COORDINATES),
	None(new Coordinates(), new Coordinates()); // default value
	
	private final Coordinates preFirstCoordinates;
	private final Coordinates lastCoordinates;
	
	private ETerritory(Coordinates preFirstCoordinates, Coordinates lastCoordinates) {
		this.preFirstCoordinates = preFirstCoordinates;
		this.lastCoordinates = lastCoordinates;
	}
	
	public boolean contains(Coordinates coordinates) {		// MAKE SURE it works correctly (Unit test / mockup)
		return (coordinates.smallerOrEqualTo(this.lastCoordinates) && !coordinates.smallerOrEqualTo(preFirstCoordinates));
	}
	
}
