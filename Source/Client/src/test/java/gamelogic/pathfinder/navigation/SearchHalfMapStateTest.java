package gamelogic.pathfinder.navigation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.navigation.SearchHalfMapState;
import client.gamelogic.pathfinder.navigation.SearchMyTerritoryForTreasure;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;

class SearchHalfMapStateTest {

    private PathFinderData mockGameData;
    private SearchHalfMapState searchState;
    private Map<Coordinates, MapNode> mockFields;
    private ETerritory mockTerritory;

    @BeforeEach
    void setUp() {
        mockGameData = mock(PathFinderData.class);
        mockTerritory = mock(ETerritory.class);

        // Using SearchHalfMapState subclass for testing
        searchState = new SearchMyTerritoryForTreasure(mockGameData, mockTerritory);

        mockFields = new HashMap<>();
        // Add mock data for coordinates and map nodes
        mockFields.put(new Coordinates(0, 0), mock(MapNode.class));
        mockFields.put(new Coordinates(1, 1), mock(MapNode.class));
    }

    @Test
    void testRemoveOutOfBoundsFields() {
        // Mock the behavior of ETerritory to ensure it returns true or false based on coordinates
        when(mockTerritory.contains(any(Coordinates.class))).thenReturn(false);

        searchState.testRemoveOutOfBoundsFields(mockFields);

        // Verify that fields outside the territory are removed
        assertTrue(mockFields.isEmpty());  // Assuming the fields outside the boundary will be removed
    }

//    @Test
//    void testDetermineNextMove() {
//        // Add test implementation here
//    }
}
