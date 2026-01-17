package com.clinicalalertsystem;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import com.clinicalalertsystem.logging.LogManager;

public class NotificationDispatcher implements Runnable {

    private final BlockingQueue<Alert> alertQueue;
    private volatile boolean running = true;

    private static final Logger logger =
            LogManager.getLogger(NotificationDispatcher.class);

    public NotificationDispatcher(BlockingQueue<Alert> alertQueue) {
        this.alertQueue = alertQueue;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Alert alert = alertQueue.take();

                try {
                    handleAlert(alert);
                } catch (Exception e) {
                    logger.severe("ALERT_HANDLER_FAILURE Room=" +
                            alert.getRoomId() +
                            " Error=" + e.getMessage());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected void handleAlert(Alert alert) {
        logger.warning(() ->
                "ALERT_DISPATCHED Room=" + alert.getRoomId() +
                " Severity=" + alert.getSeverity() +
                " Temp=" + alert.getTemperature());
    }

    public void shutdown() {
        running = false;
    }
}
