package client.mvc.controller;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.model.GameModel;
import client.mvc.model.MvcNotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;
import client.mvc.view.CLIView;

public class MvcController {
	
	private GameModel theModel;
	private CLIView theView;
	
	private MvcNotificationCollector technicalInternalsModel;
//	add a view to go with the second model
	
	public MvcController(GameModel theModel, CLIView theView, MvcNotificationCollector mvcNotificationCollector) {
		
		this.theModel = theModel;
		this.theView = theView;
		this.technicalInternalsModel = mvcNotificationCollector;

		theView.setUpListeners(theModel);
		theView.setUpListeners(technicalInternalsModel);
	}
	
	public MvcNotificationCollector getTechnicalInternalsModel () {
		return technicalInternalsModel;
	}
	
	public void updateModel (GameModel newModel) {
		theModel.updateGameModel(newModel);
	}
	
	public void setInitialHalfMap (ClientHalfMap initialHalfMap) {
		theModel.setInitialHalfMap(initialHalfMap);
	}

}
