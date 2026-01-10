package com.clinicalalertsystem;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {

        BlockingQueue<Alert> alertQueue = new LinkedBlockingQueue<>();

        List<HospitalRoom> rooms = List.of(
                new HospitalRoom("ICU-01", 18.0, 24.0),
                new HospitalRoom("ICU-02", 18.0, 24.0),
                new HospitalRoom("WARD-01", 16.0, 26.0)
        );

        ExecutorService executor = Executors.newFixedThreadPool(rooms.size() + 1);

        // Start sensor tasks
        for (HospitalRoom room : rooms) {
            executor.submit(new TemperatureSensorTask(room, alertQueue));
        }

        // Start notification dispatcher
        executor.submit(new NotificationDispatcher(alertQueue));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down ClinicalAlertSystem...");
            executor.shutdownNow();
        }));
    }
}
