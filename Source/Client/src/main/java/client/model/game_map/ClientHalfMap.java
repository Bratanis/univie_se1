package Model.GameMap;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ClientHalfMap extends GameMap {

	/**
	 * Default constructor
	 */
	public ClientHalfMap() {
	}

	/**
	 * @return
	 */
	public abstract EMove findEnemyDirection();

}