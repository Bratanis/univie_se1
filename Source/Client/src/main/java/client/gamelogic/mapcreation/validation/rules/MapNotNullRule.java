package client.gamelogic.mapcreation.validation.rules;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;

public class MapNotNullRule implements ValidationRule{

	@Override
	public void validate(ClientHalfMap map, NotificationCollector collector) {
		
		if (map == null)
			collector.addNotification("Given Map is NULL!");
	}

}
