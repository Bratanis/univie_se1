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

	ClientHalfMap mapWithOneUnreachableField;
	ClientHalfMap mapWithManyUnreachableFields;
	ClientHalfMap mapWithNoUnreachableFields;
	
	NotificationCollector collector;
	NoIslandsRule rule;
	
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
	
	@Test
	void oneUnreachableFieldTest() {
		collector.clear();
		rule.validate(mapWithOneUnreachableField, collector);
		assertFalse(collector.getNotifications().isEmpty());
	}
	
	@Test
	void manyUnreachableFieldsTest() {
		collector.clear();
		rule.validate(mapWithManyUnreachableFields, collector);
		assertFalse(collector.getNotifications().isEmpty());
	}

	@Test
	void noUnreachableFieldsTest() {
		collector.clear();
		rule.validate(mapWithNoUnreachableFields, collector);
		assertTrue(collector.getNotifications().isEmpty());
	}

}
