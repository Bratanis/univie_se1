package client.model.gamemap;

import java.io.*;
import java.util.*;

import client.customexceptions.IllegalConversionException;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class SquareGameMap extends GameMap {

	public static Coordinates getLastCoordinates() {
		return new Coordinates (9, 9);
	}
	
	/**
	 * @param mapFields
	 * @param startingCoordinates
	 */
	public SquareGameMap(HashMap mapFields, Coordinates startingCoordinates) {
		super (mapFields, startingCoordinates);
	}
	
	public SquareGameMap(HashMap mapFields) {
		super (mapFields);
	}

	/**
	 * @Override
	 * @param newMap
	 */
	public void setNewGameMap(GameMap newMap) {
		if (newMap.getClass() == this.getClass()) {
			super.setNewGameMap(newMap);
		} else {
			throw new IllegalConversionException("Tried to set type " + 
												 newMap.getClass().toString() + 
												 " in the plase of a " + 
												 this.getClass().toString());
		}
	}

	/**
	 * @Override
	 * @return
	 */
	public EMove findEnemyDirection() {
		return null;
	}

}