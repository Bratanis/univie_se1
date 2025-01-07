package client.mvc.controller;

import client.mvc.model.GameModel;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.view.CLIView;

public class MVCController {
	
	private GameModel theModel;
	private CLIView theView;
	
	
	public MVCController(GameModel theModel, CLIView theView) {

		this.theModel = theModel;
		this.theView = theView;
		
		theView.setUpListeners(theModel);
	}
	
	public void updateModel (GameModel newModel) {
		theModel.updateGameModel(newModel);
	}
	
	public void setInitialHalfMap (ClientHalfMap initialHalfMap) {
		theModel.setInitialHalfMap(initialHalfMap);
	}

}
