package Model;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class GameModel {

	/**
	 * Default constructor
	 */
	public GameModel() {
	}

	/**
	 * 
	 */
	private GameMap gameMap;

	/**
	 * 
	 */
	private GameProgress gameProgress;

	/**
	 * 
	 */
	private GameProgress gameProgress;

	/**
	 * 
	 */
	public GameModel() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	private GameMap generateValidGameMap() {
		// TODO implement here
		return null;
	}

	/**
	 * @return
	 */
	public GameMap getGameMap() {
		// TODO implement here
		return null;
	}

	/**
	 * @param newMap
	 */
	public void updateGameMap(GameMap newMap) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public EMove getNextMove() {
		// TODO implement here
		return null;
	}

	/**
	 * 
	 */
	private void loadNextMoves() {
		// TODO implement here
	}

	/**
	 * @param listener
	 */
	public void addGameMapListener(PropertyChangeListener listener) {
		// TODO implement here
	}

	/**
	 * @param listener
	 */
	public void addGameProgressListener(PropertyChangeListener listener) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public boolean gameIsOver() {
		// TODO implement here
		return false;
	}

}