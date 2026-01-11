package com.clinicalalertsystem;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

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
}
