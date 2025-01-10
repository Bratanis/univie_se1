package gamelogic.pathfinder.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import client.customexceptions.MapNavigationException;
import client.gamelogic.pathfinder.helpers.AppraisedMapField;
import client.gamelogic.pathfinder.helpers.OrientationHelper;
import client.mvc.model.gamemap.mapelements.Coordinates;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

public class OrientationHelperTest {

    private OrientationHelper orientationHelper;

    @BeforeEach
    void setUp() {
        orientationHelper = new OrientationHelper();
    }
    
    @ParameterizedTest
    @CsvSource({
        "1, 1, 1, 0, Up",    //	  	 _x0_  _x1_  _x2_
        "1, 1, 2, 1, Right", //y0	[0; 0][1; 0][2; 0]
        "1, 1, 1, 2, Down", // y1 	[0; 1][1; 1][2; 1]
        "1, 1, 0, 1, Left"  //y2  	[0; 2][1; 2][2; 2]
    })
    void testOrientation(int currentX, int currentY, int destX, int destY, EMove expectedMove) {
        Coordinates currentCoordinates = new Coordinates (currentX, currentY);
        Coordinates destinationCoordinates = new Coordinates(destX, destY);

        AppraisedMapField destinationField = mock(AppraisedMapField.class);
        when(destinationField.getCoordinates()).thenReturn(destinationCoordinates);
        when(destinationField.getTerrain()).thenReturn(ETerrain.Grass); // Can be any terrain

        Collection<EMove> directions = orientationHelper.getDirections(currentCoordinates, destinationField);

        assertEquals(2, directions.size()); // 1 move to leave current, 1 to enter grass
        directions.forEach(direction -> assertEquals(expectedMove, direction));
    } 

    @Test
    void testGetDirectionsForGrassTerrain() {
        Coordinates currentCoordinates = mock(Coordinates.class);
        Coordinates destinationCoordinates = mock(Coordinates.class);

        AppraisedMapField destinationField = mock(AppraisedMapField.class);
        when(destinationField.getTerrain()).thenReturn(ETerrain.Grass);
        when(destinationField.getCoordinates()).thenReturn(destinationCoordinates);
        when(currentCoordinates.directionTo(destinationCoordinates)).thenReturn(EMove.Up);

        Collection<EMove> directions = orientationHelper.getDirections(currentCoordinates, destinationField);

        assertEquals(2, directions.size()); // 1 move to leave current (grass by default), 1 to enter grass
        directions.forEach(direction -> assertEquals(EMove.Up, direction));
    }

    // Copy-pasted but should be okay since its only two methods that use the same logic
    @Test
    void testGetDirectionsForMountainTerrain() {
        Coordinates currentCoordinates = mock(Coordinates.class);
        Coordinates destinationCoordinates = mock(Coordinates.class);

        AppraisedMapField destinationField = mock(AppraisedMapField.class);
        when(destinationField.getTerrain()).thenReturn(ETerrain.Mountain);
        when(destinationField.getCoordinates()).thenReturn(destinationCoordinates);
        when(currentCoordinates.directionTo(destinationCoordinates)).thenReturn(EMove.Right);

        Collection<EMove> directions = orientationHelper.getDirections(currentCoordinates, destinationField);

        assertEquals(3, directions.size()); // 1 move to leave current, 2 to enter mountain
        directions.forEach(direction -> assertEquals(EMove.Right, direction));
    }

    @Test
    void testGetDirectionsForWaterTerrainThrowsException() {
        AppraisedMapField waterField = mock(AppraisedMapField.class);
        when(waterField.getTerrain()).thenReturn(ETerrain.Water);

        Exception exception = assertThrows(MapNavigationException.class, () -> {
            Coordinates currentCoordinates = mock(Coordinates.class);
            when(currentCoordinates.directionTo(any(Coordinates.class))).thenReturn(EMove.Down);

            orientationHelper.getDirections(currentCoordinates, waterField);
        }); 

        assertTrue(exception.getMessage().contains("The destination field has to be either grass or mountain"));
    }
    
}
