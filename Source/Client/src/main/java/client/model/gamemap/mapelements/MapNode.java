package client.model.gamemap.mapelements;

import java.util.Objects;

import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public class MapNode {

	/**
	 * Attributes: 
	 */
	
	private boolean hasCastle;
	private boolean hasTreasure;
	private boolean hasMe;
	private boolean hasEnemy;
	private ETerrain terrain;
	
	private boolean nearTreasure;
	private boolean nearCastle;
	
	
	public ETerrain getTerrain() {
		return terrain;
	}

	public void setTerrain(ETerrain terrain) {
		this.terrain = terrain;
	}

	
	public boolean hasCastle() {
		return hasCastle;
	}

	public void setHasCastle(boolean hasCastle) {
		this.hasCastle = hasCastle;
	}

	public boolean hasMe() {
		return hasMe;
	}

	public void setHasMe(boolean hasMe) {
		this.hasMe = hasMe;
	}

	public boolean hasEnemy() {
		return hasEnemy;
	}

	public void setHasEnemy(boolean hasEnemy) {
		this.hasEnemy = hasEnemy;
	}

	public boolean hasTreasure() {
		return this.hasTreasure;
	}
	
	public void setHasTreasure(boolean hasTreasure) {
		this.hasTreasure = hasTreasure;
	}

	
	/**
	 * Methods:
	 */
	
	/**
	 * @param terrain
	 */
	public MapNode(ETerrain terrain) {
		this.terrain = terrain;
		this.hasCastle = false;
		this.hasMe = false;
		this.hasEnemy = false;
		this.hasTreasure = false;
		this.nearTreasure = false;
		this.nearCastle = false;
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
		this.hasMe = hasMe;
		this.hasEnemy = hasEnemy;
	}

	public boolean isNearTreasure() {
		return nearTreasure;
	}

	public void setNearTreasure(boolean nearTreasure) {
		this.nearTreasure = nearTreasure;
	}

	public boolean isNearCastle() {
		return nearCastle;
	}

	public void setNearCastle(boolean nearCastle) {
		this.nearCastle = nearCastle;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    MapNode other = (MapNode) obj;
	    return hasMe == other.hasMe &&
	           hasCastle == other.hasCastle &&
	           hasTreasure == other.hasTreasure &&
	           terrain.equals(other.terrain);
	}
	@Override
	public int hashCode() {
	    return Objects.hash(hasMe, hasCastle, hasTreasure, terrain);
	}

	
}




