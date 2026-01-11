package com.clinicalalertsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TemperatureSensorTaskTest {

    @Test
    void testHighTemperatureGeneratesHighAlert() throws Exception {
        // Arrange
        HospitalRoom room = new HospitalRoom("ICU-01", 18.0, 24.0);
        BlockingQueue<Alert> queue = new LinkedBlockingQueue<>();

        // Override temperature generation to make test deterministic
        TemperatureSensorTask sensor = new TemperatureSensorTask(room, queue) {
            @Override
            protected double generateTemperature() {
                return 28.0; // > max (24) but < max + 5 (29) → HIGH
            }
        };

        Thread sensorThread = new Thread(sensor);

        // Act
        sensorThread.start();
        Thread.sleep(100); // let it run once
        sensorThread.interrupt();
        sensorThread.join();

        // Assert
        assertFalse(queue.isEmpty(), "Alert queue should not be empty");

        Alert alert = queue.take();
        assertEquals(AlertSeverity.HIGH, alert.getSeverity());
        assertEquals("ICU-01", alert.getRoomId());
    }

    @Test
    void testCriticalTemperatureGeneratesCriticalAlert() throws Exception {
        // Arrange
        HospitalRoom room = new HospitalRoom("ICU-02", 18.0, 24.0);
        BlockingQueue<Alert> queue = new LinkedBlockingQueue<>();

        TemperatureSensorTask sensor = new TemperatureSensorTask(room, queue) {
            @Override
            protected double generateTemperature() {
                return 35.0; // > max + 5 (29) → CRITICAL
            }
        };

        Thread sensorThread = new Thread(sensor);

        // Act
        sensorThread.start();
        Thread.sleep(100);
        sensorThread.interrupt();
        sensorThread.join();

        // Assert
        assertFalse(queue.isEmpty(), "Alert queue should contain a CRITICAL alert");

        Alert alert = queue.take();
        assertEquals(AlertSeverity.CRITICAL, alert.getSeverity());
        assertEquals("ICU-02", alert.getRoomId());
    }

    @Test
    void testNormalTemperatureDoesNotGenerateAlert() throws Exception {
        // Arrange
        HospitalRoom room = new HospitalRoom("WARD-01", 16.0, 26.0);
        BlockingQueue<Alert> queue = new LinkedBlockingQueue<>();

        TemperatureSensorTask sensor = new TemperatureSensorTask(room, queue) {
            @Override
            protected double generateTemperature() {
                return 22.0; // within safe range
            }
        };

        Thread sensorThread = new Thread(sensor);

        // Act
        sensorThread.start();
        Thread.sleep(100);
        sensorThread.interrupt();
        sensorThread.join();

        // Assert
        assertTrue(queue.isEmpty(), "No alert should be generated for normal temperature");
    }


}
