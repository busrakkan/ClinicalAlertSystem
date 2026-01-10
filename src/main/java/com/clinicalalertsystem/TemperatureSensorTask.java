package com.clinicalalertsystem;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class TemperatureSensorTask implements Runnable {

    private final HospitalRoom room;
    private final BlockingQueue<Alert> alertQueue;
    private final Random random = new Random();

    public TemperatureSensorTask(HospitalRoom room, BlockingQueue<Alert> alertQueue) {
        this.room = room;
        this.alertQueue = alertQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                double temperature = generateTemperature();

                AlertSeverity severity = null;

                if (temperature > room.getMaxTemperature() + 5 ||
                    temperature < room.getMinTemperature() - 5) {
                    severity = AlertSeverity.CRITICAL;
                } else if (temperature > room.getMaxTemperature() ||
                           temperature < room.getMinTemperature()) {
                    severity = AlertSeverity.HIGH;
                }

                if (severity != null) {
                    // replaced alertQueue.put() with alertQueue.offer()
                    boolean accepted = alertQueue.offer(new Alert(room.getRoomId(), temperature, severity));
                    if (!accepted) {
                        System.err.println("Alert dropped due to full queue in room " + room.getRoomId());
                    }

                }

                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // graceful shutdown
            } catch (Exception e) {
                System.err.println(
                    "Sensor failure in room " + room.getRoomId() + ": " + e.getMessage()
                );
                // loop continues → fault isolated
            }
        }
    }

    private double generateTemperature() {
        return 15 + random.nextDouble() * 20;
    }
}
