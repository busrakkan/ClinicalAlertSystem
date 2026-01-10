package com.clinicalalertsystem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {

    public static void main(String[] args) {

        PriorityBlockingQueue<Alert> alertQueue = new PriorityBlockingQueue<>();

        List<HospitalRoom> rooms = List.of(
                new HospitalRoom("ICU-01", 18.0, 24.0),
                new HospitalRoom("ICU-02", 18.0, 24.0),
                new HospitalRoom("WARD-01", 16.0, 26.0)
        );

        ExecutorService sensorExecutor = Executors.newFixedThreadPool(rooms.size());

        ExecutorService dispatcherExecutor = Executors.newSingleThreadExecutor();


        // Start sensor tasks
        for (HospitalRoom room : rooms) {
            sensorExecutor.submit(new TemperatureSensorTask(room, alertQueue));
        }

        // Start notification dispatcher
        dispatcherExecutor.submit(new NotificationDispatcher(alertQueue));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down ClinicalAlertSystem...");
            sensorExecutor.shutdownNow();
            dispatcherExecutor.shutdownNow();
        }));
    }
}
