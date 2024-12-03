package client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import client.model.gamemap.mapelements.Coordinates;


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
		this(false, false, false, new Coordinates()); // neutral value to avoid null
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
		if (newProgress == null || newProgress.equals(this)) {
			return; // No update needed
		}

		// Notify View about all changes that should be displayed
		if (this.treasureCollected != newProgress.treasureCollected) {
			boolean oldValue = this.treasureCollected;
			this.treasureCollected = newProgress.treasureCollected;
			support.firePropertyChange("treasureCollected", oldValue, this.treasureCollected);
		}

		if (this.wonGame != newProgress.wonGame) {
			boolean oldValue = this.wonGame;
			this.wonGame = newProgress.wonGame;
			support.firePropertyChange("wonGame", oldValue, this.wonGame);
		}

		if (this.lostGame != newProgress.lostGame) {
			boolean oldValue = this.lostGame;
			this.lostGame = newProgress.lostGame;
			support.firePropertyChange("lostGame", oldValue, this.lostGame);
		}

		// My current coordinates are not relevant for the view and will not fire a property change event!
		if (!this.currentCoordinates.equals(newProgress.currentCoordinates)) {
			//Coordinates oldValue = this.currentCoordinates;
			this.currentCoordinates = newProgress.currentCoordinates;
			//support.firePropertyChange("currentCoordinates", oldValue, this.currentCoordinates);
		}

		int oldRound = this.currentRound;
		this.currentRound++;
		support.firePropertyChange("currentRound", oldRound, this.currentRound);
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
	
	public boolean treasureCollected() {
		return treasureCollected;
	}
	public String toString() {
		return ("{GameProgress: "
				+ "{treasureCollected: " + treasureCollected + "}, "
				+ "{wonGame: " + wonGame + "}, "
				+ "{lostGame: " + lostGame + "}, "
				+ "{currentRound: " + currentRound + "}, "
				+ "{currentCoordinates " + currentCoordinates + "}"
				+"}");
	}

}