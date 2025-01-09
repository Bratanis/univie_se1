package gamelogic.mapcreation.validation.rules;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.gamelogic.mapcreation.validation.rules.TerrainCountRule;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

class TerrainCountRuleTest {

	// We will be testing edge cases, where the minimum isn't met by 1
	ClientHalfMap mapWith23GrassFields; // Min 24
	ClientHalfMap mapWith4MountainFields; // Min 5
	ClientHalfMap mapWith6WaterFields; // Min 7
	ClientHalfMap terrainOkButNoCastleMap;
	ClientHalfMap notEnoughFieldsMap;
	ClientHalfMap okayMap;
	
	NotificationCollector collector;
	TerrainCountRule rule;
	
	@BeforeEach
	void createRule() {
		
		this.collector = new MvcNotificationCollector();
		this.rule = new TerrainCountRule();
	}
	
	@BeforeEach
	void createMapWith23GrassFields() {
	
		 Map<Coordinates, MapNode> testMapFields = getValidTerrainCountMapWithCastleOnLastField();

		// We will leave 23 grass fields and change the rest to water so the minimum is not met
		int maxGrassForMap = 23;
		AtomicInteger grassCount = new AtomicInteger(0);

		testMapFields.forEach((coordinates, mapNode) -> {
	        if (grassCount.getAndIncrement() >= maxGrassForMap && mapNode.getTerrain() == ETerrain.Grass) {
	            // Change the terrain to Water
	            testMapFields.put(coordinates, new MapNode(ETerrain.Water));
	        }
	    });
		
		this.mapWith23GrassFields = new ClientHalfMap(testMapFields);
	}
	
	@BeforeEach
	void createMapWith4MountainFields() {
		
		 Map<Coordinates, MapNode> testMapFields = getValidTerrainCountMapWithCastleOnLastField();

		 // We will change one mountian field so that the minimum isn't met
		    AtomicBoolean changedMountainField = new AtomicBoolean(false);

		    testMapFields.forEach((coordinates, mapNode) -> {
		        if (!changedMountainField.get() && mapNode.getTerrain() == ETerrain.Mountain) {
		            testMapFields.put(coordinates, new MapNode(ETerrain.Grass));
		            changedMountainField.set(true);
		        }
		    });

		    this.mapWith4MountainFields = new ClientHalfMap(testMapFields);
	}
	
	@BeforeEach
	void createMapWith6WaterFields() {
	    Map<Coordinates, MapNode> testMapFields = getValidTerrainCountMapWithCastleOnLastField();

	    // We will change one water field so that the minimum isn't met
	    AtomicBoolean changedWaterField = new AtomicBoolean(false);

	    testMapFields.forEach((coordinates, mapNode) -> {
	        if (!changedWaterField.get() && mapNode.getTerrain() == ETerrain.Water) {
	            testMapFields.put(coordinates, new MapNode(ETerrain.Grass));
	            changedWaterField.set(true);
	        }
	    });

	    this.mapWith6WaterFields = new ClientHalfMap(testMapFields);
	}

	@BeforeEach
	void createMapWithOkTerrainButNoCastle() {
		
		 Map<Coordinates, MapNode> testMapFields = getValidTerrainCountMapWithCastleOnLastField();
		 testMapFields.get(ClientHalfMap.LAST_COORDINATES).setHasCastle(false);
		 
		 this.terrainOkButNoCastleMap = new ClientHalfMap(testMapFields);
	}
	
	/**
	 * Will remove the first grass field on the map;
	 * This way of handling the problem was suggested by 
	 * ChatGPT 4o, but was improved by me later;
	 */
	@BeforeEach
	void createMapWithNotEnoughFields() {
	    Map<Coordinates, MapNode> testMapFields = getValidTerrainCountMapWithCastleOnLastField();

	    // Identify the key to be removed
	    Coordinates keyToRemove = testMapFields.entrySet().stream()
	        .filter(entry -> (entry.getValue().getTerrain() == ETerrain.Grass && !entry.getValue().hasCastle()))
	        .map(Map.Entry::getKey)
	        .findFirst()
	        .orElse(null);

	    // Remove the identified key if found
	    if (keyToRemove != null) {
	        testMapFields.remove(keyToRemove);
	    }

	    this.notEnoughFieldsMap = new ClientHalfMap(testMapFields);
	}

	
	@BeforeEach
	void createOkMap() {
		
		Map<Coordinates, MapNode> testMapFields = getValidTerrainCountMapWithCastleOnLastField();
		 
		this.okayMap = new ClientHalfMap(testMapFields);	
	}

	
	private Map<Coordinates, MapNode> getValidTerrainCountMapWithCastleOnLastField(){
		
		int waterToBePlaced = 7;
		int mountainsToBePlaced = 5;
		
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		AtomicInteger waterCount = new AtomicInteger(0);
		AtomicInteger mountainCount = new AtomicInteger(0);

	    // Iterate over the map and after the 23rd field, change all remaining fields to water 
		 testMapFields.forEach((coordinates, mapNode) -> {
		        if (waterCount.getAndIncrement() < waterToBePlaced) {
		            testMapFields.put(coordinates, new MapNode(ETerrain.Water));
		        } else if (mountainCount.getAndIncrement() < mountainsToBePlaced) {
		            testMapFields.put(coordinates, new MapNode(ETerrain.Mountain));
		        } 
		 });
		 
		// Put the castle on the last node, since it will be grass
		testMapFields.get(ClientHalfMap.LAST_COORDINATES).setHasCastle(true);
		
		return testMapFields;	
	}

	
	@Test
	void notEnoughGrassFieldsTest() {
		collector.clear();
		rule.validate(mapWith23GrassFields, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void notEnoughMountainFieldsTest() {
		collector.clear();
		rule.validate(mapWith4MountainFields, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void notEnoughWaterFieldsTest() {
		collector.clear();
		rule.validate(mapWith6WaterFields, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void terrainOkButNoCastleTest() {
		collector.clear();
		rule.validate(terrainOkButNoCastleMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void notEnoughFieldsTest() {
		collector.clear();
		rule.validate(notEnoughFieldsMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void okayMapTest() {
		collector.clear();
		rule.validate(okayMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.isEmpty());
	}


}







