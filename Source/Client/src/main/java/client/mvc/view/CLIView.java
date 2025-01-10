package client.mvc.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.mvc.model.GameModel;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.GameMap;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

public abstract class CLIView {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CLIView() {
        // No internal state like `treasureFound` is maintained
    }

    public void setUpListeners(GameModel gameModel) {
        gameModel.addListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                handleGameModelPropertyChange(evt, gameModel);
            }
        });
    }
    
    public void setUpListeners(MvcNotificationCollector technicalInternalsModel) {
		
    	technicalInternalsModel.addListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                handleTechnicalInternalsPropertyChange(evt, technicalInternalsModel);
            }
        });
		
	}
    
    
    

    public void handleTechnicalInternalsPropertyChange(PropertyChangeEvent event, MvcNotificationCollector technicalInternalsModel) {
        if ("Added notification".equals(event.getPropertyName())) {
            String latestNotification = (String) event.getNewValue();
            System.out.println(latestNotification); // Print only the latest notification every time a change gets fired!
        }
    }
 

	public void handleGameModelPropertyChange(PropertyChangeEvent event, GameModel model) {
        String propertyName = event.getPropertyName();

        switch (propertyName) {
            case "Map Changed!" -> {
                GameMap newMap = (GameMap) event.getNewValue();
                printGameMap(newMap, model.isTreasureCollected());
            }
            case "Treausre Collected!" -> {
                if ((boolean) event.getNewValue()) {
                    printTreasureCollected();
                    // Optionally trigger map re-render if needed
                    printGameMap(model.getGameMap(), true);
                }
            }
            case "Game Won!" -> {
                if ((boolean) event.getNewValue()) {
                    printWinMessage();
                }
            }
            case "Game Lost!" -> {
                if ((boolean) event.getNewValue()) {
                    printLossMessage();
                }
            }
            case "Round Changed!" -> {
                System.out.println("Round: " + event.getNewValue());
            }
            default -> logger.warn("Unhandled property change: {}", propertyName);
        }
    }

    private void printGameMap(GameMap map, boolean treasureCollected) {
        System.out.println("__________________________________________________________________\n");

        printXIndexRow(map.getLastCoordinates().getX());

        String printableMapRow = "";

        int numOfRows = map.getLastCoordinates().getY();

        for (int currentY = 0; currentY <= numOfRows; ++currentY) {
            printableMapRow += "Y" + currentY + " ";
            printableMapRow += getFormattedLine(currentY, map, treasureCollected);
        }

        System.out.println(printableMapRow);
    }

    private String getFormattedLine(int yRow, GameMap map, boolean treasureCollected) {
        int elementsPerLine = map.getLastCoordinates().getX();

        String formattedMapLine = "";

        for (int xCol = 0; xCol <= elementsPerLine; ++xCol) {
            Coordinates targetCoordinates = new Coordinates(xCol, yRow);
            MapNode targetNode = map.getNodeAt(targetCoordinates);
            if (targetNode == null) {
                logger.warn("TargetNode with coordinates " + targetCoordinates + " is out of bounds!");
            }
            formattedMapLine += getFieldAsCliRender(targetNode, treasureCollected);
        }

        return formattedMapLine.concat("\n");
    }
    

    protected abstract String getFieldAsCliRender(MapNode mapNode, boolean treasureCollected);

    private void printXIndexRow(int lastMapX) {
        String xIndexRow = "X: ";
        for (int xCol = 0; xCol <= lastMapX; ++xCol) {
            xIndexRow += String.format("%02d", xCol) + " ";
        }
        System.out.println(xIndexRow);
    }

    public void printWinMessage() {
        System.out.println("CONGRATS! YOU WON THE GAME!");
    }

    public void printLossMessage() {
        System.out.println("SORRY! YOU LOST THE GAME!");
    }
    
    public void printTreasureCollected() {
    	System.out.println("Treasure collected!");
    }

	
}
