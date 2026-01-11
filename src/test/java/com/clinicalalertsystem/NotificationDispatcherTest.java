package com.clinicalalertsystem;

import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class NotificationDispatcherTest {

    @Test
    void testDispatcherProcessesAlertsInPriorityOrder() throws Exception {
        // Arrange
        PriorityBlockingQueue<Alert> queue = new PriorityBlockingQueue<>();

        Alert highAlert = new Alert("WARD-01", 27.0, AlertSeverity.HIGH);
        Alert criticalAlert = new Alert("ICU-01", 33.0, AlertSeverity.CRITICAL);

        // Intentionally add HIGH first
        queue.put(highAlert);
        queue.put(criticalAlert);

        NotificationDispatcher dispatcher = new NotificationDispatcher(queue);
        Thread dispatcherThread = new Thread(dispatcher);

        // Act
        dispatcherThread.start();
        Thread.sleep(200); // allow dispatcher to process alerts
        dispatcher.shutdown();
        dispatcherThread.interrupt();
        dispatcherThread.join();

        // Assert
        assertTrue(queue.isEmpty(), "Dispatcher should drain all alerts from the queue");
    }
}
