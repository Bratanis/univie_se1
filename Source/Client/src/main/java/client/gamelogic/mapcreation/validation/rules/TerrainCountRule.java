package client.gamelogic.mapcreation.validation.rules;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.mapelements.Coordinates;

/**
 * "Jede Kartenhälfte muss mindestens 10% Bergfelder, 48% Wiesenfelder, 14% Wasserfelder und 2% Burg beinhalten."
 * Validating the existence of the castle goes here because of the way it is worded in the task description
 */
public class TerrainCountRule implements ValidationRule{
	
	private final int MIN_GRASS_FIELDS = 24;
	private final int MIN_MOUNTAIN_FIELDS = 5;
	private final int MIN_WATER_FIELDS = 7;
	private final int TOTAL_NUM_OF_FIELDS = 50;

	@Override
	public void validate(ClientHalfMap testMap, NotificationCollector collector) {
		int grass = 0, mountain = 0, water = 0, totalFieldCount = 0;
		boolean castlePresent = false;
		

        for (Coordinates coordinates : testMap.getAllCoordinates()) {
            totalFieldCount++;
            
            if (testMap.hasCastleAt(coordinates)) {
                castlePresent = true;
            } 
            
            switch (testMap.getTerrainAt(coordinates)) {
                case Grass -> grass++;
                case Mountain -> mountain++;
                case Water -> water++;
            }
        }

        if (grass < MIN_GRASS_FIELDS) 
        	collector.addNotification("Map has fewer grass fields than the minimum required (" + grass + " out of " + MIN_GRASS_FIELDS + ").");
        if (mountain < MIN_MOUNTAIN_FIELDS) 
        	collector.addNotification("Map has fewer mountain fields than the minimum required (" + mountain + " out of " + MIN_MOUNTAIN_FIELDS + ").");
        if (water < MIN_WATER_FIELDS) 
        	collector.addNotification("Map has fewer water fields than the minimum required (" + water + " out of " + MIN_WATER_FIELDS + ").");
        if (totalFieldCount != TOTAL_NUM_OF_FIELDS) 
        	collector.addNotification("Map should have " + TOTAL_NUM_OF_FIELDS + " but has " + totalFieldCount + " out of " + totalFieldCount);
        if (!castlePresent) 
	        collector.addNotification("No castle is present on the map.");
	   
	}

}
