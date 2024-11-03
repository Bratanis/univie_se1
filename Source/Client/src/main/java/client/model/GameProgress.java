package client.model;

import client.model.gamemap.Coordinates;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class GameProgress {

	/**
	 * Attributes:
	 */

	private boolean treasureCollected;

	private boolean wonGame;

	private boolean lostGame;

	private int currentRound;

	private Coordinates currentCoordinates;

	private final PropertyChangeSupport support;

	/**
	 * Methods:
	 */

	public GameProgress() {
		this(false, false, false, new Coordinates(-1, -1)); // neutral value to avoid null
	}
	
	public GameProgress (boolean treasureCollected, boolean wonGame, boolean lostGame, Coordinates currentCoordinates) {
		this.treasureCollected = treasureCollected;
		this.wonGame = wonGame;
		this.lostGame = lostGame;
		this.currentRound = 0;
		this.currentCoordinates = currentCoordinates;
		this.support = new PropertyChangeSupport(this);
	}

	public void updateGameProgress(GameProgress newProgress) {
		if (newProgress != this) {
		
		}
	}
		
	public boolean equals (GameProgress other) {
	    if (this == other) 
	    	return true;
	    if (other == null)
	    	return false;
	    return (
	    		this.treasureCollected == other.treasureCollected && 
	    		this.wonGame == other.wonGame && 
	    		this.lostGame == other.lostGame &&
	    		this.currentCoordinates == other.currentCoordinates);
	}
	
	/**
	 * 
	 */
	public void nextRound() {
		++currentRound;
	}

	/**
	 * @param listener
	 */
	public void addListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	/**
	 * @return
	 */
	public Coordinates getCurrentCoordinates() {
		return currentCoordinates;
	}

	/**
	 * @return
	 */
	public boolean gameIsOver() {
		if (wonGame || lostGame)
			return true;
		else
			return false;
	}

}