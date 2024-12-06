package client.view;

import client.model.GameModel;
import client.model.GameProgress;
import client.model.gamemap.GameMap;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class CLIView {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean treasureFound = false;

	/**
	 * 
	 */
	private PropertyChangeListener gameProgressListener;

	
	
	private PropertyChangeListener mapChangeListener;
	
	 
	public CLIView() {
		this.gameProgressListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	//logger.debug("Property changed: " + evt.getPropertyName());
                printGameProgress(evt);
            }
        };

        this.mapChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	logger.debug("Property changed: " + evt.getPropertyName());
                printGameMap((GameMap) evt.getNewValue());
            }
        };
	}

	public void setUpListeners(GameModel gameModel) {
        gameModel.addGameProgressListener(this.gameProgressListener);
        gameModel.addGameMapListener(this.mapChangeListener);
    }

	private void printGameProgress(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		Object newValue = evt.getNewValue();

		switch (propertyName) {
			case "currentRound" -> {
				System.out.println("Round: " + newValue);
			}
			case "treasureCollected" -> {
				if ((boolean) newValue) {
					treasureFound = true;
					System.out.println("Treasure Collected!");
				}
			}
			case "wonGame" -> {
				if ((boolean) newValue) {
					printWinMessage();
				}
			}
			case "lostGame" -> {
				if ((boolean) newValue) {
					printLossMessage();
				}
			}
			default -> logger.warn("Unknown Game Progress Update: {}", propertyName);
		}

	}

	/**
	 * @param map
	 */
	private void printGameMap(GameMap map) {
		System.out.println("__________________________________________________________________\n");
		
		printXIndexRow(map.getLastCoordinates().getX());

		String printableMapRow = "";

		int numOfRows =  map.getLastCoordinates().getY();
		
		for (int currentY = 0; currentY <= numOfRows; ++currentY) {
			printableMapRow += " [Y" + currentY + "] ";
			printableMapRow += getFormattedLine (currentY, map);
		}

		System.out.println(printableMapRow);
	}

	/**
	 * helper mehtod that returns a given row of the map
	 * 
	 * @param yRow
	 * @param gameMap
	 * @return
	 */
	private String getFormattedLine(int yRow, GameMap map) {

		int elementsPerLine =  map.getLastCoordinates().getX(); 
		
		String formattedMapLine = "";
	
//    			logger.info("mapFields: " + gameMap.toString());

		for (int xCol = 0; xCol <= elementsPerLine; ++xCol) {
			Coordinates targetCoordinates = new Coordinates(xCol, yRow);
			MapNode targetNode = map.getNodeAt(targetCoordinates);
		    		if (targetNode == null) {
						logger.warn("targetNode with coordinates " + targetCoordinates + " is out of bounds!");
					}
			formattedMapLine += mapNodeToASCII(targetNode);
		}

		return formattedMapLine.concat("\n");
	}

	private void printXIndexRow(int lastMapX) {
	    String xIndexRow = " [X:] ";
	    for (int xCol = 0; xCol <= lastMapX; ++xCol) {
	        xIndexRow += " [" + String.format("%02d", xCol) + "] "; // suggested by ChatGPT
	    }
	    System.out.println(xIndexRow);
	}


	/**
	 * @param mapNode 
	 * @return
	 */
	private String mapNodeToASCII(MapNode mapNode) {
		if (mapNode.hasCastle()) {
			return " _IHI_";
		} else if (mapNode.hasTreasure()) {
			return " _[$]_";
		}  else if (mapNode.hasEnemy() && mapNode.hasMe()) {
			return "!%#@✴&";
		} else if (mapNode.hasMe()) {
			if (treasureFound)
				return " ($‿$)";
			else
				return " (°‿°)";
		} else if (mapNode.hasEnemy()) {
			return " (`ʖ̯´)";
		} else {

			switch (mapNode.getTerrain()) {
			case ETerrain.Grass:
				return " _____";
			case ETerrain.Water:
				return " ~~~~~";
			case ETerrain.Mountain:
				return " A^A^A";
			default:
				return " !!!!!"; // should never be reached!
			}
		}
	}


	/**
	 * 
	 */
	private void printWinMessage() {
		System.out.println("CONGRATS! YOU WON THE GAME!");
	}

	/**
	 * 
	 */
	private void printLossMessage() {
		System.out.println("SORRY! YOU LOST THE GAME!");
	}


}