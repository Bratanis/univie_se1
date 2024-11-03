package client.model.gamemap;

import java.io.*;
import java.util.*;

import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public class MapNode {

	/**
	 * Attributes: 
	 */
	private ETerrain terrain;
	private boolean hasTreasure;
	private boolean hasCastle;
	private boolean hasMe;
	private boolean hasEnemy;

	
	/**
	 * Methods:
	 */
	
	/**
	 * @param terrain
	 */
	public MapNode(ETerrain terrain) {
		this.terrain = terrain;
		this.hasTreasure = false;
		this.hasCastle = false;
		this.hasMe = false;
		this.hasEnemy = false;
	}

	/**
	 * 
	 * @param terrain
	 * @param hasCastle
	 */
	public MapNode(ETerrain terrain, boolean hasCastle) {
		this(terrain);
		this.hasCastle = hasCastle;
	}
	
	/**
	 * @param terrain
	 * @param hasCastle
	 * @param hasTreasure
	 * @param hasMe
	 * @param hasEnemy
	 */
	public MapNode(ETerrain terrain, boolean hasCastle, boolean hasTreasure, boolean hasMe, boolean hasEnemy) {
		this(terrain, hasCastle);
		this.hasTreasure = hasTreasure;
		this.hasMe = hasMe;
		this.hasEnemy = hasEnemy;
	}
	
}




