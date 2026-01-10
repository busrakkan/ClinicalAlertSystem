package com.clinicalalertsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class TemperatureReadingTest {

    @Test
    void readingContainsRoomIdAndTemperature() {
        TemperatureReading reading = new TemperatureReading("ICU-01", 21.5);

        assertEquals("ICU-01", reading.getRoomId());
        assertEquals(21.5, reading.getTemperature());
        assertNotNull(reading.getTimestamp());
    }
}
