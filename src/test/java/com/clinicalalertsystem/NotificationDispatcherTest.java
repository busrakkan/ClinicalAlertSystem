package com.clinicalalertsystem;

import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDispatcherTest {

    @Test
    void testDispatcherProcessesCriticalBeforeHigh() throws Exception {
        PriorityBlockingQueue<Alert> queue = new PriorityBlockingQueue<>();

        Alert highAlert = new Alert("WARD-01", 27.0, AlertSeverity.HIGH);
        Alert criticalAlert = new Alert("ICU-01", 33.0, AlertSeverity.CRITICAL);

        // Add HIGH first on purpose
        queue.put(highAlert);
        queue.put(criticalAlert);

        AtomicReference<AlertSeverity> firstProcessed = new AtomicReference<>();

        NotificationDispatcher dispatcher = new NotificationDispatcher(queue) {
            @Override
            protected void handleAlert(Alert alert) {
                // record the first processed alert
                firstProcessed.compareAndSet(null, alert.getSeverity());
            }
        };

        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();

        Thread.sleep(300); // allow processing
        dispatcher.shutdown();
        dispatcherThread.interrupt();
        dispatcherThread.join();

        assertEquals(
                AlertSeverity.CRITICAL,
                firstProcessed.get(),
                "Dispatcher should process CRITICAL alerts before HIGH ones"
        );
    }
}
