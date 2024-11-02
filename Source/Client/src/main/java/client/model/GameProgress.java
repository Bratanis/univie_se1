package Model;

import Model.GameMap.Coordinates;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class GameProgress {

	/**
	 * Default constructor
	 */
	public GameProgress() {
	}

	/**
	 * 
	 */
	private boolean treasureFound;

	/**
	 * 
	 */
	private boolean wonGame;

	/**
	 * 
	 */
	private boolean lostGame;

	/**
	 * 
	 */
	private int currentRound;

	/**
	 * 
	 */
	private Coordinates currentCoordinates;

	/**
	 * 
	 */
	private PropertyChangeSupport support;

	/**
	 * 
	 */
	public void nextRound() {
		// TODO implement here
	}

	/**
	 * @param listener
	 */
	public void addListener(PropertyChangeListener listener) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public Coordinates getCurrentCoordinates() {
		// TODO implement here
		return null;
	}

	/**
	 * @return
	 */
	public boolean gameIsOver() {
		// TODO implement here
		return false;
	}

}