package com.clinicalalertsystem;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import com.clinicalalertsystem.logging.LogManager;

public class NotificationDispatcher implements Runnable {

    private final BlockingQueue<Alert> alertQueue;
    private volatile boolean running = true;
    private static final Logger logger = LogManager.getLogger(NotificationDispatcher.class);

    public NotificationDispatcher(BlockingQueue<Alert> alertQueue) {
        this.alertQueue = alertQueue;
    }

    @Override
    public void run() {
        while (running || !alertQueue.isEmpty()) {
            try {
                // Take next alert (blocks if queue is empty)
                Alert alert = alertQueue.take();

                // Process alert (simulate notification)
                processAlert(alert);

                // Log alert dispatch
                logger.severe(() -> "ALERT_DISPATCHED Room=" + alert.getRoomId() +
                                    " Severity=" + alert.getSeverity() +
                                    " Temperature=" + alert.getTemperature() +
                                    " Time=" + alert.getTimestamp());
            } catch (InterruptedException e) {
                // Graceful shutdown
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // Fault isolation: one failed dispatch does not stop dispatcher
                logger.severe("DISPATCHER_FAILURE Error=" + e.getMessage());
            }
        }
        logger.info("NotificationDispatcher has shut down.");
    }

    private void processAlert(Alert alert) {
        // Here you could send emails, SMS, or trigger hospital system notifications
        // For demo, we just print
        System.out.println("[ALERT] Room: " + alert.getRoomId() +
                           " | Temp: " + alert.getTemperature() +
                           "°C | Severity: " + alert.getSeverity() +
                           " | Time: " + alert.getTimestamp());
    }

    public void shutdown() {
        running = false;
    }
}
