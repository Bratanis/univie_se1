package gamelogic.pathfinder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.gamelogic.pathfinder.PathFinder;
import client.gamelogic.pathfinder.helpers.MapTerritories;
import client.gamelogic.pathfinder.helpers.PathFinderData;
import client.gamelogic.pathfinder.navigation.NavigationState;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.model.gamemap.GameMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.ETerritory;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;

class PathFinderTest {
	
	MapTerritories mockTerritories;
	
	PathFinderData mockData;
	
	PathFinder pathFinder;
    
    @BeforeEach
    void setUp() {
    	mockTerritories = mock(MapTerritories.class);
        when(mockTerritories.getMyTerritory()).thenReturn(ETerritory.TopLeftSide);
        when(mockTerritories.getEnemyTerriotry()).thenReturn(ETerritory.BottomSide);
        
        mockData = mock(PathFinderData.class);
        when(mockData.getMyPosition()).thenReturn(new Coordinates());
        when(mockData.getSurroundings()).thenReturn(Collections.emptyMap());
        when(mockData.isTreasureCollected()).thenReturn(false);
        
        pathFinder = new PathFinder(mockData, mockTerritories);
    }

    @Test
    void testGetUndefinedInstance() {
        PathFinder pathFinder = PathFinder.getUndifinedInstance();
        assertFalse(pathFinder.isDefined());
    }

    @Test
    void testConstructor() {
        // create mocks
        PathFinderData mockData = mock(PathFinderData.class);

        // Create a spy on the PathFinder instance
        PathFinder pathFinderSpy = spy(new PathFinder(mockData, mockTerritories));

        // Validate object got constructed as expected
        assertTrue(pathFinderSpy.isDefined(), "PathFinder should be marked as defined after construction");

        assertNotNull(pathFinderSpy.getCurrentNavigationState(), "Current navigation state should be initialized");
     }
 


    @Test
    void testUpdateData() {
        PathFinderData newData = mock(PathFinderData.class);
        when(newData.getMyPosition()).thenReturn(new Coordinates(1, 1));
        when(newData.getSurroundings()).thenReturn(Collections.emptyMap());
        when(newData.isTreasureCollected()).thenReturn(true);
        
//        PathFinder pathFinder = new PathFinder(mockData, mockTerritories);

        pathFinder.updateData(newData);

        verify(mockData).setMyPosition(any());
        verify(mockData).setSurroundings(any());
        verify(mockData).setTreasureCollected(anyBoolean());
    }

    @Test
    void testLoadAndGetNextMove() {
 
//        NavigationState mockState = mock(NavigationState.class);
        
        GameMap exampleMap = new ClientHalfMap(ClientHalfMap.getGrassOnlyMapFields());
        Coordinates myPos = new Coordinates(2,3);
        Map<Coordinates, MapNode> surroundings = exampleMap.getFieldsAround(myPos);
        
        PathFinderData newData = new PathFinderData(myPos, surroundings, false);
        
        PathFinder newPathFinder = new PathFinder(newData, mockTerritories);
        
//        pathFinder.updateData(newData);
 
        EMove nextMove = newPathFinder.getNextMove();

//        verify(mockState).determineNextMoves(any());
        assertNotNull(nextMove);
    }

    @Test
    void testGetNextMoveOnUndefinedPF() {
        PathFinder pathFinder = PathFinder.getUndifinedInstance();

        assertThrows(AssertionError.class, pathFinder::getNextMove);
    }

//    @Test
//    void testReevaluateCurrentState() {
//        PathFinder pathFinder = PathFinder.getUndifinedInstance();
//        assertThrows(NoSuchElementException.class, pathFinder::reevaluateCurrentState);
//    }
}
