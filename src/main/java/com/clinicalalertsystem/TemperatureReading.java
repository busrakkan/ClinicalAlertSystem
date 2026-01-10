package com.clinicalalertsystem;

import java.time.Instant;

public class TemperatureReading {

    private final String roomId;
    private final double temperature;
    private final Instant timestamp;

    public TemperatureReading(String roomId, double temperature) {
        this.roomId = roomId;
        this.temperature = temperature;
        this.timestamp = Instant.now();
    }

    public String getRoomId() {
        return roomId;
    }

    public double getTemperature() {
        return temperature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
