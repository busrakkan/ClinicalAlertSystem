package com.clinicalalertsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class AlertTest {

    @Test
    void alertContainsCorrectData() {
        Alert alert = new Alert("ICU-01", 30.0, AlertSeverity.HIGH);

        assertEquals("ICU-01", alert.getRoomId());
        assertEquals(30.0, alert.getTemperature());
        assertEquals(AlertSeverity.HIGH, alert.getSeverity());
        assertNotNull(alert.getTimestamp());
    }
}
