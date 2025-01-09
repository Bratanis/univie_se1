package client.gamelogic.mapcreation.validation.rules;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;
import client.mvc.model.gamemap.ClientHalfMap;

public interface ValidationRule {

    void validate(ClientHalfMap map, NotificationCollector collector);
}
