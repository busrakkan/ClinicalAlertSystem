package com.clinicalalertsystem;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HospitalRoomTest {

    @Test
    void unsafeTemperatureIsDetectedFromReading() {
        HospitalRoom room = new HospitalRoom("ICU-01", 18.0, 24.0);
        TemperatureReading reading = new TemperatureReading("ICU-01", 30.0);

        assertTrue(room.isReadingUnsafe(reading));
    }

    @Test
    void safeTemperatureIsAccepted() {
        HospitalRoom room = new HospitalRoom("ICU-01", 18.0, 24.0);
        TemperatureReading reading = new TemperatureReading("ICU-01", 22.0);

        assertFalse(room.isReadingUnsafe(reading));
    }

    @Test
    void readingFromDifferentRoomThrowsException() {
        HospitalRoom room = new HospitalRoom("ICU-01", 18.0, 24.0);
        TemperatureReading reading = new TemperatureReading("ICU-02", 30.0);

        assertThrows(IllegalArgumentException.class,
                () -> room.isReadingUnsafe(reading));
    }
}
