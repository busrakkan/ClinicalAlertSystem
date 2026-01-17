package com.clinicalalertsystem;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import com.clinicalalertsystem.logging.LogManager;

public class TemperatureSensorTask implements Runnable {

    private final HospitalRoom room;
    private final BlockingQueue<Alert> alertQueue;
    private final Random random = new Random();
    private static final Logger logger = LogManager.getLogger(TemperatureSensorTask.class);

    public TemperatureSensorTask(HospitalRoom room, BlockingQueue<Alert> alertQueue) {
        this.room = room;
        this.alertQueue = alertQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                double temperature = generateTemperature();
                logger.info(() -> "TEMP_READING Room=" + room.getRoomId() +
                                   " Temperature=" + temperature + "C");

                AlertSeverity tempSeverity = null;
                if (temperature > room.getMaxTemperature() + 5 ||
                    temperature < room.getMinTemperature() - 5) {
                    tempSeverity = AlertSeverity.CRITICAL;
                } else if (temperature > room.getMaxTemperature() ||
                           temperature < room.getMinTemperature()) {
                    tempSeverity = AlertSeverity.HIGH;
                }

                final AlertSeverity severity = tempSeverity;

                if (severity != null) {
                    Alert alert = new Alert(room.getRoomId(), temperature, severity);
                    boolean accepted = alertQueue.offer(alert);

                    if (accepted) {
                        logger.warning(() -> "ALERT_CREATED Room=" + alert.getRoomId() +
                                             " Severity=" + alert.getSeverity() +
                                             " Temperature=" + temperature);
                    } else {
                        logger.severe(() -> "ALERT_DROPPED Room=" + room.getRoomId() +
                                           " Severity=" + severity);
                    }
                }

                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            } catch (Exception e) {
                logger.severe("SENSOR_FAILURE Room=" + room.getRoomId() +
                              " Error=" + e.getMessage());
            }
        }
    }

    protected double generateTemperature() {
        return 15 + random.nextDouble() * 20; // 15°C – 35°C
    }
}
