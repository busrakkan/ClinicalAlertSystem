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

                if (room.isReadingUnsafe(reading)) {
                    Alert alert = new Alert(
                            room.getRoomId(),
                            temperature,
                            AlertSeverity.HIGH
                    );
                    alertQueue.put(alert); // thread-safe
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
