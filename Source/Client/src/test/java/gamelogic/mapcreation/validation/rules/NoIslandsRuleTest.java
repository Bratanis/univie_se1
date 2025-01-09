package gamelogic.mapcreation.validation.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.gamelogic.mapcreation.validation.rules.NoIslandsRule;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

class NoIslandsRuleTest {

	private ClientHalfMap mapWithOneUnreachableField;
	private ClientHalfMap mapWithManyUnreachableFields;
	private ClientHalfMap mapWithNoUnreachableFields;
	
	private NotificationCollector collector;
	private NoIslandsRule rule;
	
	private String expectedNotification = "Found a field that cannot be visited! The map has islands!";
	
	
	@BeforeEach
	void createRule() {
		
		this.collector = new MvcNotificationCollector();
		this.rule = new NoIslandsRule();
	}
	
	@BeforeEach
	void createMapWithOneUnreachableField(){
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		// We will make the top left corner to be an island
		List<Coordinates> fieldsToBeSetAsWater = new ArrayList <> ();
		fieldsToBeSetAsWater.add(new Coordinates (0, 1));
		fieldsToBeSetAsWater.add(new Coordinates (1, 0));
		
		testMapFields.forEach((coordinate, mapNode) -> {
	        if (fieldsToBeSetAsWater.contains(coordinate)) {
	            testMapFields.put(coordinate, new MapNode(ETerrain.Water));
	        }
	    });
		
		this.mapWithOneUnreachableField = new ClientHalfMap(testMapFields);
	}
	
	@BeforeEach
	void createMapWithManyUnreachableFields(){
		
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		
		// We will split the map in two with a vertical water line
		int xOfwaterLineToDivideTheMap = 5;
		testMapFields.forEach((coordinate, mapNode) -> {
	        if (coordinate.getX() == xOfwaterLineToDivideTheMap) {
	            testMapFields.put(coordinate, new MapNode(ETerrain.Water));
	        }
	    });
		this.mapWithManyUnreachableFields = new ClientHalfMap(testMapFields);
	}

	@BeforeEach
	void createMapWithNoUnreachableFields(){
		Map<Coordinates, MapNode> testMapFields = ClientHalfMap.getGrassOnlyMapFields();
		this.mapWithNoUnreachableFields = new ClientHalfMap(testMapFields);
	}
	
	
	private List<String> getRuleNotifications(ClientHalfMap testMap) {
		collector.clear();
		rule.validate(testMap, collector);
		return collector.getNotifications();
	}
	
	
	@Test
	void oneUnreachableFieldTest() {

		List<String> notifications = getRuleNotifications(mapWithOneUnreachableField);
		
		//Make sure that notification is the right one!
		assertTrue(notifications.getFirst().startsWith(expectedNotification));
	}

	
	
	@Test
	void manyUnreachableFieldsTest() {

		List<String> notifications = getRuleNotifications(mapWithOneUnreachableField);
		
		//Make sure that notification is the right one!
		assertTrue(notifications.getFirst().startsWith(expectedNotification));
	}

	@Test
	void noUnreachableFieldsTest() {

		List<String> notifications = getRuleNotifications(mapWithOneUnreachableField);
		
		//Make sure that notification is the right one!
		assertTrue(notifications.getFirst().startsWith(expectedNotification));
	}

}
