package client.mvc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import client.gamelogic.mapcreation.validation.NotificationCollector.NotificationCollector;

public class MvcNotificationCollector implements NotificationCollector{
	
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	
    private final List<String> notifications = new ArrayList<>(); // Final to ensure Consistency (reference to the list cannot be changed)
	
    /**
	 * @param listener
	 */
	public void addListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);	
	}
    
    @Override
	public void addNotification(String message) {
		 String detailedMessage = formatMessageWithStackTrace(message);
	     notifications.add(detailedMessage);
	}

	@Override
	public List<String> getNotifications() {
        return new ArrayList<>(notifications); // Return a copy to ensure immutability
	}

	@Override
	public void clear() {
		notifications.clear();
	}
	
	 /**
     * Appends the stacktrace to the message "Verweis auf relevante Validierungslogik [nur individuell relevante "Validierungs-" Methode & Klasse laut Stacktrace]"
     */
    private String formatMessageWithStackTrace(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        
        // Exclude irrelevant frames, such as this method and the Thread.getStackTrace call
        int index = 2; // Adjust based on the calling context
        StringBuilder formattedMessage = new StringBuilder(message).append("\nStack Trace:\n");

        // Include only relevant parts of the stack trace
        for (; index < stackTrace.length; index++) {
            StackTraceElement element = stackTrace[index];
            if (isApplicationCode(element)) { // Optional filtering
                formattedMessage
                    .append("  at ")
                    .append(element.toString())
                    .append("\n");
            }
        }
        return formattedMessage.toString();
    }

    /**
     * Optional: Filters the stack trace to include only application-relevant classes.
     */
    private boolean isApplicationCode(StackTraceElement element) {
        String className = element.getClassName();
        return className.startsWith("client.") || className.startsWith("messagesbase.");
    }
}
