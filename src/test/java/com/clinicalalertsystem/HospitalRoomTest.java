package com.clinicalalertsystem;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HospitalRoomTest {

    @Test
    void temperatureOutOfRangeIsDetected() {
        HospitalRoom room = new HospitalRoom("ICU-01", 18.0, 24.0);

        assertTrue(room.isTemperatureOutOfRange(30.0));
        assertFalse(room.isTemperatureOutOfRange(22.0));
    }
}
