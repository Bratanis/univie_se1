package Model.GameMap;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class SquareGameMap extends GameMap {

	/**
	 * Default constructor
	 */
	public SquareGameMap() {
	}

	/**
	 * @return
	 */
	public abstract EMove findEnemyDirection();

}