package com.clinicalalertsystem;

import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueTest {

    @Test
    void testPriorityQueueOrder() throws InterruptedException {
        // Create queue for Alert objects
        PriorityBlockingQueue<Alert> queue = new PriorityBlockingQueue<>();

        // Insert alerts in "wrong" order
        Alert highAlert = new Alert("ICU-01", 25.0, AlertSeverity.HIGH);
        Alert criticalAlert = new Alert("ICU-02", 32.0, AlertSeverity.CRITICAL);

        queue.put(highAlert);
        queue.put(criticalAlert);

        // Take first alert from queue
        Alert firstOut = queue.take();

        // CRITICAL should come out first
        assertEquals(AlertSeverity.CRITICAL, firstOut.getSeverity());
        assertEquals("ICU-02", firstOut.getRoomId());

        // Next alert should be HIGH
        Alert secondOut = queue.take();
        assertEquals(AlertSeverity.HIGH, secondOut.getSeverity());
        assertEquals("ICU-01", secondOut.getRoomId());
    }
}
