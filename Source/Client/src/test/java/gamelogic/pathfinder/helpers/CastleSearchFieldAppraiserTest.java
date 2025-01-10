package gamelogic.pathfinder.helpers;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.customexceptions.MapNavigationException;
import client.gamelogic.pathfinder.helpers.appraiser.CastleSearchFieldAppraiser;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

class CastleSearchFieldAppraiserTest {

    private CastleSearchFieldAppraiser appraiser;
    private Collection<Coordinates> visitedCoordinates;

    @BeforeEach
    void setUp() {
        visitedCoordinates = new ArrayList<>();
        appraiser = new CastleSearchFieldAppraiser(visitedCoordinates);
    }

    @Test
    void testAtGoal() {
        // Mock MapNode to focus testing on one class
        MapNode mapNodeWithCastle = mock(MapNode.class);
        when(mapNodeWithCastle.hasCastle()).thenReturn(true);

        MapNode mapNodeWithoutCastle = mock(MapNode.class);
        when(mapNodeWithoutCastle.hasCastle()).thenReturn(false);

        assertTrue(appraiser.atGoal(mapNodeWithCastle));
        assertFalse(appraiser.atGoal(mapNodeWithoutCastle));
    }

    @Test
    void testNearGoal() {
        // Mock MapNode to focus testing on one class
        MapNode mapNodeNearCastle = mock(MapNode.class);
        when(mapNodeNearCastle.isNearCastle()).thenReturn(true);

        MapNode mapNodeNotNearCastle = mock(MapNode.class);
        when(mapNodeNotNearCastle.isNearCastle()).thenReturn(false);

        assertTrue(appraiser.nearGoal(mapNodeNearCastle));
        assertFalse(appraiser.nearGoal(mapNodeNotNearCastle));
    }
    
    @Test
    void testGetHighestPriorityFieldWithNullInput() {
    	
        Exception exception = assertThrows(MapNavigationException.class, () -> {
            appraiser.getBestMapField(null);
        });
        assertTrue(exception.getMessage().contains("Cannot getBestMapField because unappraisedFields is NULL!"), exception.getMessage());
    }
    
    @Test
    void testGetHighestPriorityFieldWithEmptyMap() {
    	
    	
        Exception exception = assertThrows(MapNavigationException.class, () -> {
            appraiser.getBestMapField(Collections.emptyMap());
        });
        assertTrue(exception.getMessage().contains("Cannot get highest priority field"), exception.getMessage());
    }
    
    @Test
    void testGetHighestPriorityFieldWithEmptyList() {

    	Exception exception = assertThrows(MapNavigationException.class, () -> {
            appraiser.testGetHighestPriorityField(Collections.emptyList());
        });
        assertTrue(exception.getMessage().contains("Cannot get highest priority field"), exception.getMessage());
    }

//    /**
//     * Integration test unfinished since its not required
//     */
//    @Test
//    void testAssignFieldPriorityIntegration() {
//        // Mock MapNode
//        MapNode goalNode = mock(MapNode.class);
//        when(goalNode.hasCastle()).thenReturn(true);
//        when(goalNode.getTerrain()).thenReturn(ETerrain.Grass);
//
//        MapNode nearGoalNode = mock(MapNode.class);
//        when(nearGoalNode.isNearCastle()).thenReturn(true);
//        when(nearGoalNode.getTerrain()).thenReturn(ETerrain.Mountain);
//
//        MapNode visitedNode = mock(MapNode.class);
//        Coordinates visitedCoordinatesObj = new Coordinates(1, 1);
//        visitedCoordinates.add(visitedCoordinatesObj);
//
//        Map<Coordinates, MapNode> fields = new HashMap<>();
//        fields.put(new Coordinates(0, 0), goalNode);
//        fields.put(new Coordinates(0, 1), nearGoalNode);
//        fields.put(visitedCoordinatesObj, visitedNode);
//
//        AppraisedMapField bestField = appraiser.getBestMapField(fields);
//
//    }
}
