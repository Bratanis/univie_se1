package mvc.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import client.mvc.model.MvcNotificationCollector;

public class MvcNotificationCollectorTest {

    @Mock
    private PropertyChangeListener mockListener;

    private MvcNotificationCollector notificationCollector;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationCollector = new MvcNotificationCollector();
        notificationCollector.addListener(mockListener);
    }

    @Test
    public void testNotificationEventTriggered() {
        
        String notificationMessage = "Test Notification";
        notificationCollector.addNotification(notificationMessage);

        // Make sure the listener reacted to the change
        ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(mockListener, times(1)).propertyChange(eventCaptor.capture());

        // Verify the data received by the listener is the new notification added to the collector
        PropertyChangeEvent capturedEvent = eventCaptor.getValue();
        assertEquals(notificationMessage, capturedEvent.getNewValue()); 
    }
}
