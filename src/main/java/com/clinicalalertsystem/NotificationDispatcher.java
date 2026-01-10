package com.clinicalalertsystem;

import java.util.concurrent.BlockingQueue;

public class NotificationDispatcher implements Runnable {

    private final BlockingQueue<Alert> alertQueue;

    public NotificationDispatcher(BlockingQueue<Alert> alertQueue) {
        this.alertQueue = alertQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                Alert alert = alertQueue.take(); // blocks safely
                dispatch(alert);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dispatch(Alert alert) {
        System.out.println(
                "[ALERT] Room: " + alert.getRoomId() +
                " | Temp: " + alert.getTemperature() +
                "°C | Severity: " + alert.getSeverity() +
                " | Time: " + alert.getTimestamp()
        );
    }
}
