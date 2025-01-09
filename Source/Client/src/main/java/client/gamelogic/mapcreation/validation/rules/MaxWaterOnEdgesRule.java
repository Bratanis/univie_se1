package client.gamelogic.mapcreation.validation.rules;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import messagesbase.messagesfromclient.ETerrain;

/**
 * Map can have max 2 water on the short sides and max 4 water on the long sides.
 * We are checking opposite sides simultaneously to reduce the num of times we need to iterate through the map fields
 * @param gameMap
 * @return
 */
public class MaxWaterOnEdgesRule implements ValidationRule{

	@Override
	public void validate(ClientHalfMap testMap, NotificationCollector collector) {
		
		checkTopAndBottomForWater(testMap, collector);
		checkLeftAndRightForWater(testMap, collector);
	}
	
	private void checkTopAndBottomForWater(ClientHalfMap gameMap, NotificationCollector collector) {
		
		int firstX = 0; // column
		int lastX = gameMap.getLastCoordinates().getX();

		int firstY = 0; // row
		int lastY = gameMap.getLastCoordinates().getY();
		
		int waterCountTop = 0;
		int waterCountBottom = 0;
		for (int x = firstX; x <= lastX; ++x) {
			Coordinates topTargetCoordinates = new Coordinates(x, firstY);
			Coordinates bottomTargetCoordinates = new Coordinates(x, lastY);
			
			if (gameMap.getTerrainAt(topTargetCoordinates) == ETerrain.Water) {
				++waterCountTop;
			}
			if (gameMap.getTerrainAt(bottomTargetCoordinates) == ETerrain.Water) {
				++waterCountBottom;
			}
		}
	
		int maxWaterOnSides = lastX/2;
		if (waterCountTop > maxWaterOnSides) {
			collector.addNotification("Water on the top side is: " + waterCountTop + " but should be <= " + maxWaterOnSides);
		}
		if (waterCountBottom > maxWaterOnSides){
			collector.addNotification("Water on the bottom side is: " + waterCountBottom + " but should be <= " + maxWaterOnSides);
		}
	}
	
	
	private void checkLeftAndRightForWater(ClientHalfMap gameMap, NotificationCollector collector) {
		
		int firstX = 0; // column
		int lastX = gameMap.getLastCoordinates().getX();

		int firstY = 0; // row
		int lastY = gameMap.getLastCoordinates().getY();
		
		int waterCountLeft = 0;
		int waterCountRight = 0;
		
		for (int y = firstY; y <= lastY; ++y) {
			Coordinates leftTargetCoordinates = new Coordinates(firstX, y);
			Coordinates rightTargetCoordinates = new Coordinates(lastX, y);
			
			if (gameMap.getTerrainAt(leftTargetCoordinates) == ETerrain.Water) {
				++waterCountLeft;
			}
			if (gameMap.getTerrainAt(rightTargetCoordinates) == ETerrain.Water) {
				++waterCountRight;
			}
		}
		int maxWaterOnSides = lastY/2;
		if (waterCountLeft > maxWaterOnSides ) {
			collector.addNotification("Water on the left side is: " + waterCountLeft + " but should be <= " + maxWaterOnSides);
		}
		if (waterCountRight > maxWaterOnSides) {
			collector.addNotification("Water on the right side is: " + waterCountRight + " but should be <= " + maxWaterOnSides);
		}
	}
}
