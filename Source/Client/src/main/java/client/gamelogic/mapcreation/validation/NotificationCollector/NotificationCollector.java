package client.gamelogic.mapcreation.validation.NotificationCollector;

import java.util.List;

public interface NotificationCollector {
	void addNotification(String message);
    List<String> getNotifications();
    void clear();
}
