package com.clinicalalertsystem;

public class HospitalRoom {

    private final String roomId;
    private final double minTemperature;
    private final double maxTemperature;

    public HospitalRoom(String roomId, double minTemperature, double maxTemperature) {
        this.roomId = roomId;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isTemperatureOutOfRange(double temperature) {
        return temperature < minTemperature || temperature > maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public boolean isReadingUnsafe(TemperatureReading reading) {
        if (!roomId.equals(reading.getRoomId())) {
            throw new IllegalArgumentException("TemperatureReading does not belong to this room");
        }
        return isTemperatureOutOfRange(reading.getTemperature());
    }
}
