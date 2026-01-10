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
        try {
            while (!Thread.currentThread().isInterrupted()) {

                double temperature = generateTemperature();
                TemperatureReading reading =
                        new TemperatureReading(room.getRoomId(), temperature);

                AlertSeverity severity = null;

                // Use room’s min/max for severity calculation
                if (temperature > room.getMaxTemperature() + 5 || temperature < room.getMinTemperature() - 5) {
                    severity = AlertSeverity.CRITICAL;
                } else if (temperature > room.getMaxTemperature() || temperature < room.getMinTemperature()) {
                    severity = AlertSeverity.HIGH;
                }

                // Only produce an alert if necessary
                if (severity != null) {
                    alertQueue.put(new Alert(room.getRoomId(), temperature, severity));
                }

                Thread.sleep(2000); // simulate sensor delay
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private double generateTemperature() {
        return 15 + random.nextDouble() * 20; // 15°C to 35°C
    }
}
