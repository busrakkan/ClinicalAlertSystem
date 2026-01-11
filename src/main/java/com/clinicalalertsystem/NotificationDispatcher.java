package com.clinicalalertsystem;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.clinicalalertsystem.logging.LogManager;

public class NotificationDispatcher implements Runnable {

    private final BlockingQueue<Alert> alertQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private static final Logger logger =
            LogManager.getLogger(NotificationDispatcher.class);

    public NotificationDispatcher(BlockingQueue<Alert> alertQueue) {
        this.alertQueue = alertQueue;
    }

    @Override
    public void run() {
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            try {
                Alert alert = alertQueue.poll(500, TimeUnit.MILLISECONDS);

                if (alert != null) {
                    handleAlert(alert);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // graceful shutdown
            } catch (Exception e) {
                logger.severe("DISPATCHER_FAILURE Error=" + e.getMessage());
            }
        }
    }

    /**
     * Hook method for processing alerts.
     * Can be overridden in tests.
     */
    protected void handleAlert(Alert alert) {
        logger.warning(() ->
                "ALERT_DISPATCHED " +
                "Room=" + alert.getRoomId() +
                " Temp=" + alert.getTemperature() + "C " +
                "Severity=" + alert.getSeverity() +
                " Time=" + Instant.now()
        );
    }

    public void shutdown() {
        running.set(false);
    }
}
