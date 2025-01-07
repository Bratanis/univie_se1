package client.gamelogic.pathfinder.helpers;

import client.mvc.model.gamemap.mapelements.ETerritory;

public class MapTerritories {

	ETerritory myTerritory;
	ETerritory enemyTerriotry;
	
	
	
	
	public MapTerritories() {
		myTerritory = ETerritory.None;
		enemyTerriotry = ETerritory.None;
	}


	/**
	 * 
	 * @param myTerritory
	 * @param enemyTerriotry
	 */
	public MapTerritories(ETerritory myTerritory, ETerritory enemyTerriotry) {
		super();
		this.myTerritory = myTerritory;
		this.enemyTerriotry = enemyTerriotry;
	}
	
	
	public ETerritory getMyTerritory() {
		return myTerritory;
	}
	public void setMyTerritory(ETerritory myTerritory) {
		this.myTerritory = myTerritory;
	}
	public ETerritory getEnemyTerriotry() {
		return enemyTerriotry;
	}
	public void setEnemyTerriotry(ETerritory enemyTerriotry) {
		this.enemyTerriotry = enemyTerriotry;
	}
	
	
}
