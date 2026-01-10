package com.clinicalalertsystem;

import java.time.Instant;

public class Alert implements Comparable<Alert> {

    private final String roomId;
    private final double temperature;
    private final Instant timestamp;
    private final AlertSeverity severity;

    public Alert(String roomId, double temperature, AlertSeverity severity) {
        this.roomId = roomId;
        this.temperature = temperature;
        this.severity = severity;
        this.timestamp = Instant.now();
    }

        public AlertSeverity getSeverity() {
        return severity;
    }

    @Override
    public int compareTo(Alert other) {
        return this.severity.ordinal() - other.severity.ordinal();
    }

    @Override
    public String toString() {
        return "[ALERT] Room: " + roomId +
               " | Temp: " + temperature +
               "°C | Severity: " + severity +
               " | Time: " + timestamp;
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
