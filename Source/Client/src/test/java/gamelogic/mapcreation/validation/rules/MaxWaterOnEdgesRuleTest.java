package gamelogic.mapcreation.validation.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.gamelogic.mapcreation.validation.rules.MaxWaterOnEdgesRule;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

class MaxWaterOnEdgesRuleTest {

	private ClientHalfMap tooMuchWaterOnBottomMap;
	private ClientHalfMap tooMuchWaterOnTopMap;
	private ClientHalfMap tooMuchWaterOnLeftMap;
	private ClientHalfMap tooMuchWaterOnRightMap;
	private ClientHalfMap okayMap;

	NotificationCollector collector;
	private MaxWaterOnEdgesRule rule;
	
	@BeforeEach
	void createRule() {
		
		this.collector = new MvcNotificationCollector();
		this.rule = new MaxWaterOnEdgesRule();
	}
	
	@BeforeEach
	void createTooMuchWaterOnBottomMap() {
		int lastY = ClientHalfMap.LAST_COORDINATES.getY();
		
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		testMapFields.forEach((coordinate, mapNode) -> {
	        if (coordinate.getY() == lastY) {
	            testMapFields.put(coordinate, new MapNode(ETerrain.Water));
	        }
	    });
		
		tooMuchWaterOnBottomMap = new ClientHalfMap(testMapFields);
	}
	
	@BeforeEach
	void createTooMuchWaterOnTopMap() {
		int firstY = 0;
		
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		testMapFields.forEach((coordinate, mapNode) -> {
	        if (coordinate.getY() == firstY) {
	            testMapFields.put(coordinate, new MapNode(ETerrain.Water));
	        }
	    });
		tooMuchWaterOnTopMap = new ClientHalfMap(testMapFields);
	}

	@BeforeEach
	void createTooMuchWaterOnLeftMap() {
		int firstX = 0;
		
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		testMapFields.forEach((coordinate, mapNode) -> {
	        if (coordinate.getX() == firstX) {
	            testMapFields.put(coordinate, new MapNode(ETerrain.Water));
	        }
	    });
		tooMuchWaterOnLeftMap = new ClientHalfMap(testMapFields);
	}

	@BeforeEach
	void createTooMuchWaterOnRightMap() {
		int lastX = ClientHalfMap.LAST_COORDINATES.getX();
		
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		testMapFields.forEach((coordinate, mapNode) -> {
	        if (coordinate.getX() == lastX) {
	            testMapFields.put(coordinate, new MapNode(ETerrain.Water));
	        }
	    });
		tooMuchWaterOnRightMap = new ClientHalfMap(testMapFields);
	}

	@BeforeEach
	void createOkayMap() {
		Map<Coordinates, MapNode> grassOnlyMapFields = ClientHalfMap.getGrassOnlyMapFields();
		okayMap = new ClientHalfMap(grassOnlyMapFields);
	}
	
	
	
	
	@Test
	void tooMuchWaterOnBottomTest() {
		collector.clear();
		rule.validate(tooMuchWaterOnBottomMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void tooMuchWaterOnTopTest() {
		collector.clear();
		rule.validate(tooMuchWaterOnTopMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void tooMuchWaterOnLeftTest() {
		collector.clear();
		rule.validate(tooMuchWaterOnLeftMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void tooMuchWaterOnRightTest() {
		collector.clear();
		rule.validate(tooMuchWaterOnRightMap, collector);
		List<String> notifications = collector.getNotifications();
		assertTrue(notifications.size() == 1);
	}
	
	@Test
	void noWaterOnEdgesTest() {
		collector.clear();
		rule.validate(okayMap, collector);
		assertTrue(collector.getNotifications().isEmpty());
	}
	

	

	
}
