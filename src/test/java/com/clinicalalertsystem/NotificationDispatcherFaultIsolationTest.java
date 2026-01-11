package com.clinicalalertsystem;

import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationDispatcherFaultIsolationTest {

    @Test
    void dispatcherContinuesAfterHandlerException() throws Exception {
        PriorityBlockingQueue<Alert> queue = new PriorityBlockingQueue<>();

        // Two alerts: dispatcher must process both
        queue.add(new Alert("ICU-01", 30.0, AlertSeverity.CRITICAL));
        queue.add(new Alert("WARD-01", 27.0, AlertSeverity.HIGH));

        NotificationDispatcher dispatcher = new NotificationDispatcher(queue) {
            private int handled = 0;

            @Override
            protected void handleAlert(Alert alert) {
                handled++;
                if (handled == 1) {
                    throw new RuntimeException("Simulated handler failure");
                }
            }
        };

        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();

        // Give dispatcher time to process
        Thread.sleep(500);

        dispatcherThread.interrupt();
        dispatcherThread.join(1000);

        // Dispatcher should continue and drain the queue
        assertTrue(queue.isEmpty(),
                "Dispatcher should continue processing after handler failure");
    }
}

