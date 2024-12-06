package client.controller;

import client.customexceptions.UserInputException;
import client.model.GameModel;
import client.model.gamemap.GameMap;
import client.network.ClientNetwork;
import client.network.servercompatlayer.ModelDataEnvelope;

import java.io.*;
import java.util.*;

import client.view.CLIView;
import messagesbase.messagesfromclient.EMove;

/**
 * 
 */
public class GameController {

	private ClientNetwork theNetwork;
	private GameModel theModel;
	private CLIView theView;

	/**
	 * @param network 
	 * @param model 
	 * @param view
	 */
	public GameController(ClientNetwork network, GameModel model, CLIView view) {
		this.theNetwork = network;
		this.theModel = model;
		this.theView = view;
	}


	public void initializeGame() throws UserInputException{
		if(!theNetwork.isRegistered()) {
			theNetwork.registerClient();
		}
		theView.setUpListeners(theModel);
	}
	

	/**
	 * 
	 */
	public void initialMapExchange() {
		GameMap clientHalfMap = theModel.getGameMap();
		theNetwork.busyWaitForMyTurn();
		theNetwork.sendLocalMapToServer(clientHalfMap);
		ModelDataEnvelope newData = theNetwork.getModelData();
		theModel.updateGameModel(newData);
		
	}

	/**
	 * Keep sending the next move determined by the pathfinder (inside the model) and updating the data 
	 * with the server response until the game is over 
	 */
	public void startGame() {
		while (!theModel.gameIsOver()) {
			theNetwork.busyWaitForMyTurn();
			EMove nextMove = theModel.getNextMove();
			//System.out.println("MY NEXT MOVE: " + nextMove); // FOR DEBUGGING
			theNetwork.sendMove(nextMove);
			ModelDataEnvelope serverResponse = theNetwork.getModelData();
			theModel.updateGameModel(serverResponse);
		}
	}

}





