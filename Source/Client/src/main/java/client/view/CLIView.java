package View;

import Model.GameProgress;
import Model.GameMap.GameMap;
import Model.GameMap.MapNode;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class CLIView {

	/**
	 * Default constructor
	 */
	public CLIView() {
	}

	/**
	 * 
	 */
	private PropertyChangeListener gameChangeListener;

	/**
	 * @param gameProgress
	 */
	private void printGameProgress(GameProgress gameProgress) {
		// TODO implement here
	}

	/**
	 * @param map
	 */
	private void printGameMap(GameMap map) {
		// TODO implement here
	}

	/**
	 * @param mapNode 
	 * @return
	 */
	private String mapNodeToASCII(MapNode mapNode) {
		// TODO implement here
		return "";
	}

	/**
	 * 
	 */
	private void printWinMessage() {
		// TODO implement here
	}

	/**
	 * 
	 */
	private void printLossMessage() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public PropertyChangeListener getGameChangeListener() {
		// TODO implement here
		return null;
	}

}