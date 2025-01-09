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
import messagesbase.messagesfromclient.ETerrain;

public class CLIView {

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
    
    
    

    public void handleTechnicalInternalsPropertyChange(PropertyChangeEvent evt, MvcNotificationCollector technicalInternalsModel) {
		
    	// Every time a notification gets added to the list, print it out to the console
    	System.out.println(technicalInternalsModel.getNotifications().getLast());
	}

	private void handleGameModelPropertyChange(PropertyChangeEvent evt, GameModel model) {
        String propertyName = evt.getPropertyName();

        switch (propertyName) {
            case "Map Changed!" -> {
                GameMap newMap = (GameMap) evt.getNewValue();
                printGameMap(newMap, model.isTreasureCollected());
            }
            case "Treausre Collected!" -> {
                if ((boolean) evt.getNewValue()) {
                    System.out.println("Treasure Collected!");
                    // Optionally trigger map re-render if needed
                    printGameMap(model.getGameMap(), true);
                }
            }
            case "Game Won!" -> {
                if ((boolean) evt.getNewValue()) {
                    printWinMessage();
                }
            }
            case "Game Lost!" -> {
                if ((boolean) evt.getNewValue()) {
                    printLossMessage();
                }
            }
            case "Round Changed!" -> {
                System.out.println("Round: " + evt.getNewValue());
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
            printableMapRow += " [Y" + currentY + "] ";
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
            formattedMapLine += mapNodeToASCII(targetNode, treasureCollected);
        }

        return formattedMapLine.concat("\n");
    }

    private String mapNodeToASCII(MapNode mapNode, boolean treasureCollected) {
        if (mapNode.hasCastle()) {
            return " _IHI_";
        } else if (mapNode.hasTreasure()) {
            return " _[$]_";
        } else if (mapNode.hasEnemy() && mapNode.hasMe()) {
            return "!%#@✴&";
        } else if (mapNode.hasMe()) {
            return treasureCollected ? " ($‿$)" : " (°‿°)";
        } else if (mapNode.hasEnemy()) {
            return " (`ʖ̯´)";
        } else {
            return switch (mapNode.getTerrain()) {
                case ETerrain.Grass -> " _____";
                case ETerrain.Water -> " ~~~~~";
                case ETerrain.Mountain -> " A^A^A";
                default -> " !!!!!"; // Should never be reached!
            };
        }
    }

    private void printXIndexRow(int lastMapX) {
        String xIndexRow = " [X:] ";
        for (int xCol = 0; xCol <= lastMapX; ++xCol) {
            xIndexRow += " [" + String.format("%02d", xCol) + "] ";
        }
        System.out.println(xIndexRow);
    }

    private void printWinMessage() {
        System.out.println("CONGRATS! YOU WON THE GAME!");
    }

    private void printLossMessage() {
        System.out.println("SORRY! YOU LOST THE GAME!");
    }

	
}
