package com.clinicalalertsystem;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationDispatcherStressTest {

    @Test
    void dispatcherHandlesHighLoadOfAlerts() throws InterruptedException {
        int totalAlerts = 1000;

        PriorityBlockingQueue<Alert> queue = new PriorityBlockingQueue<>();
        CountDownLatch processedLatch = new CountDownLatch(totalAlerts);

        // Dispatcher with overridden handler to count processed alerts
        NotificationDispatcher dispatcher = new NotificationDispatcher(queue) {
            @Override
            protected void handleAlert(Alert alert) {
                processedLatch.countDown();
            }
        };

        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();

        // Produce alerts (mix of severities)
        for (int i = 0; i < totalAlerts; i++) {
            AlertSeverity severity =
                    (i % 3 == 0) ? AlertSeverity.CRITICAL : AlertSeverity.HIGH;

            queue.put(new Alert("ROOM-" + i, 30.0 + i, severity));
        }

        // Assert dispatcher processed everything within time limit
        boolean completed = processedLatch.await(3, TimeUnit.SECONDS);

        dispatcher.shutdown();
        dispatcherThread.interrupt();

        assertTrue(completed, "Dispatcher should process all alerts under load");
    }
}
