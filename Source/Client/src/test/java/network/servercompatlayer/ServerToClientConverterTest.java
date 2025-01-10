package network.servercompatlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import client.mvc.model.gamemap.mapelements.MapNode;
import client.network.servercompatlayer.ServerDataEnvelope;
import client.network.servercompatlayer.ServerToClientDataConverter;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

class ServerToClientDataConverterTest {

	 ServerToClientDataConverter converter;
	
	
	 @BeforeEach
	 void setUp(){
		 this.converter = new ServerToClientDataConverter();
	 }
	 
//	@Test
    @ParameterizedTest
    @CsvSource({
        "true, true, false",    // Treasure collected, won game
        "true, false, true",   // Treasure collected, lost game
        "false, false, false", // No treasure, game not yet over
        "true, false, false" // Treasure collected, game not yet over
    })
    void testGetServerDataEnvelope(boolean collectedTreasure, boolean expectedWon, boolean expectedLost) {
    	
    	EPlayerGameState currentState = determineEPlayerGameState(expectedWon, expectedLost);
    	
        // we mock the map to reduce variability (complex object)
    	FullMap mockFullMap = getMockFullMap();
        
    	ServerDataEnvelope result = converter.getServerDataEnvelope(mockFullMap, collectedTreasure, currentState);

        assertNotNull(result);
        assertEquals(collectedTreasure, result.isTreasureCollected(), "Treasure collection error");
        assertEquals(expectedWon, result.isGameWon(), "Game won error");
        assertEquals(expectedLost, result.isGameLost(), "Game lost error");
        assertEquals((expectedWon || expectedLost), result.isGameOver(), "Game over error");
    }
    
    private FullMap getMockFullMap() {
    	
    	// Mocking the nodes so that the converter doesnt throw an exception
    	List<FullMapNode> mockNodes = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                FullMapNode mockNode = mock(FullMapNode.class);

                when(mockNode.getX()).thenReturn(x);
                when(mockNode.getY()).thenReturn(y);
                when(mockNode.getTreasureState()).thenReturn(ETreasureState.NoOrUnknownTreasureState);
                when(mockNode.getFortState()).thenReturn(EFortState.NoOrUnknownFortState);
                if (x == y && x == 0)
                	when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.MyPlayerPosition);
                else 
                	when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.NoPlayerPresent);

                mockNodes.add(mockNode);
            }
        }
        
        // Pin my position somewhere
        
        FullMap fullMap = mock(FullMap.class);
        when(fullMap.getMapNodes()).thenReturn(mockNodes); 
        
        return fullMap;
    }
    

    private EPlayerGameState determineEPlayerGameState(boolean wonGame, boolean lostGame) {
		if (wonGame && !lostGame)
			return EPlayerGameState.Won;
		else if (lostGame && !wonGame)
			return EPlayerGameState.Lost;
		else if (!lostGame && ! wonGame)
			return EPlayerGameState.MustAct;
		else 
			throw new IllegalArgumentException("A Player cannot be winning and losing at the same time!");
	}




	@ParameterizedTest
    @CsvSource({
    	// Position: MyPlayerPosition
        "MyPlayerPosition, MyTreasureIsPresent, NoOrUnknownFortState, true, true, false",
        "MyPlayerPosition, MyTreasureIsPresent, EnemyFortPresent, true, true, true",
        "MyPlayerPosition, NoOrUnknownTreasureState, NoOrUnknownFortState, true, false, false",
        "MyPlayerPosition, NoOrUnknownTreasureState, EnemyFortPresent, true, false, true",

        // Position: BothPlayerPosition
        "BothPlayerPosition, MyTreasureIsPresent, NoOrUnknownFortState, true, true, false",
        "BothPlayerPosition, MyTreasureIsPresent, EnemyFortPresent, true, true, true",
        "BothPlayerPosition, NoOrUnknownTreasureState, NoOrUnknownFortState, true, false, false",
        "BothPlayerPosition, NoOrUnknownTreasureState, EnemyFortPresent, true, false, true",

        // Position: NoPlayerPresent
        "NoPlayerPresent, MyTreasureIsPresent, NoOrUnknownFortState, false, true, false",
        "NoPlayerPresent, MyTreasureIsPresent, EnemyFortPresent, false, true, true",
        "NoPlayerPresent, NoOrUnknownTreasureState, NoOrUnknownFortState, false, false, false",
        "NoPlayerPresent, NoOrUnknownTreasureState, EnemyFortPresent, false, false, true"
    })
    void testGetLocalMapNode(String positionState, String treasureState, String fortState, 
                             boolean expectedHasMe, boolean expectedHasTreasure, boolean expectedHasCastle) {

		FullMapNode fullMapNode = mock(FullMapNode.class);
        when(fullMapNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.valueOf(positionState));
        when(fullMapNode.getTreasureState()).thenReturn(ETreasureState.valueOf(treasureState));
        when(fullMapNode.getFortState()).thenReturn(EFortState.valueOf(fortState));

        ServerToClientDataConverter converter = new ServerToClientDataConverter();
        MapNode localNode = converter.getLocalMapNode(fullMapNode);

        assertEquals(expectedHasMe, localNode.hasMe(), "Node should have had my position pinned on it!");
        assertEquals(expectedHasTreasure, localNode.hasTreasure(), "Node should have had treasure pinned on it!");
        assertEquals(expectedHasCastle, localNode.hasCastle(), "Node should have had a castle pinned on it!");
    }
}
