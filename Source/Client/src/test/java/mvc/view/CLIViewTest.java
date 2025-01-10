package mvc.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import client.mvc.model.GameModel;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.GameMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import client.mvc.view.CLIView;

class CLIViewTest {
	
	private CLIView spyCliView;
	
	/**
	 * Rendering of the fields is tested on the concrete class EmojiCLIView
	 */
	@BeforeEach
	void setUp(){
		spyCliView = Mockito.spy(new CLIView() {
	    @Override
	    protected String getFieldAsCliRender(MapNode mapNode, boolean treasureCollected) {
	        return " ";
	    }
	    });
	}

	@Test
	void testTechnicalInternalsModelObserving() {
		MvcNotificationCollector techInternalsModel = new MvcNotificationCollector();
		spyCliView.setUpListeners(techInternalsModel);
		String testNotification = "New notification";
	    
	    techInternalsModel.addNotification(testNotification);;

	    // Capture the call to handleTechnicalInternalsPropertyChange
	    ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
	    verify(spyCliView).handleTechnicalInternalsPropertyChange(eventCaptor.capture(), eq(techInternalsModel));

	    // Extract and verify the captured event
	    PropertyChangeEvent capturedEvent = eventCaptor.getValue();
	    assertEquals("Added notification", capturedEvent.getPropertyName());
	    String capturedValue = (String) capturedEvent.getNewValue();
	    assertTrue(capturedValue.contains(testNotification));
		
	}

	     
	 @Test
	 void testGameModelMapChangeObserving() {
		// Create a GameModel and set up the listener
	    GameModel gameModel = new GameModel();
	    spyCliView.setUpListeners(gameModel);

	    // Create a mock GameMap and configure it to return valid coordinates
	    GameMap newGameMap = getMockMap();

	    // Simulate a game map update
	    gameModel.updateGameMap(newGameMap);

	    // Capture the call to handleGameModelPropertyChange
	    ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
	    verify(spyCliView).handleGameModelPropertyChange(eventCaptor.capture(), eq(gameModel));

	    // Extract and verify the captured event
	    PropertyChangeEvent capturedEvent = eventCaptor.getValue();
	    assertEquals("Map Changed!", capturedEvent.getPropertyName());
	    assertEquals(newGameMap, capturedEvent.getNewValue());
	
	}
	 
	 private GameMap getMockMap() {
		// Create a mock GameMap and configure it to return valid coordinates
		    GameMap newGameMap = mock(GameMap.class);
		    Coordinates mockCoordinates = mock(Coordinates.class);
		    when(mockCoordinates.getX()).thenReturn(0);
		    when(mockCoordinates.getY()).thenReturn(0);
		    when(newGameMap.getLastCoordinates()).thenReturn(mockCoordinates);
		    return newGameMap;
	 }
	 
	 @ParameterizedTest
	 @CsvSource({
		    "Treausre Collected!, true, Treasure Collected!",
		    "Game Won!, true, CONGRATS! YOU WON THE GAME!",
		    "Game Lost!, true, SORRY! YOU LOST THE GAME!"
//		    "Round Changed!, 1, Round: 1"
		})
	 void testOtherGameModelPropertiesObserving(String propertyName, boolean newValue, String expectedOutput) {

		 GameModel gameModel = new GameModel(getMockMap());
	    spyCliView.setUpListeners(gameModel);

	    // Trigger the property change on the GameModel
	    switch (propertyName) {
	        case "Treausre Collected!" -> gameModel.setTreasureCollected();
	        case "Game Won!" -> gameModel.setGameWon();
	        case "Game Lost!" -> gameModel.setGameLost();
//	        case "Round Changed!" -> gameModel.nextRound();
	        default -> throw new IllegalArgumentException("Unsupported property name: " + propertyName);
	    }

	    // Capture the call to handleGameModelPropertyChange
	    ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
	    verify(spyCliView).handleGameModelPropertyChange(eventCaptor.capture(), eq(gameModel));

	    // Extract and verify the captured event
	    PropertyChangeEvent capturedEvent = eventCaptor.getValue();
	    assertEquals(propertyName, capturedEvent.getPropertyName());
	    assertEquals(newValue, capturedEvent.getNewValue());

	    // Additional behavior verification
	    switch (propertyName) {
	        case "Treausre Collected!" -> verify(spyCliView).printTreasureCollected();
	        case "Game Won!" -> verify(spyCliView).printWinMessage();
	        case "Game Lost!" -> verify(spyCliView).printLossMessage();
	        case "Round Changed!" -> assertEquals(newValue, capturedEvent.getNewValue());
	    }
	 }

}













