package com.clinicalalertsystem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {

    public static void main(String[] args) {

        // 1. Create a bounded priority queue for alerts
        PriorityBlockingQueue<Alert> alertQueue = new PriorityBlockingQueue<>(100);

        // 2. Define hospital rooms with thresholds
        List<HospitalRoom> rooms = List.of(
                new HospitalRoom("ICU-01", 18.0, 24.0),
                new HospitalRoom("ICU-02", 18.0, 24.0),
                new HospitalRoom("WARD-01", 16.0, 26.0)
        );

        // 3. Executor for sensor tasks (one thread per room)
        ExecutorService sensorExecutor = Executors.newFixedThreadPool(rooms.size());

        // 4. Executor for dispatcher (single thread)
        ExecutorService dispatcherExecutor = Executors.newSingleThreadExecutor();

        // 5. Start sensor tasks
        for (HospitalRoom room : rooms) {
            sensorExecutor.submit(new TemperatureSensorTask(room, alertQueue));
        }

        // 6. Start notification dispatcher
        NotificationDispatcher dispatcher = new NotificationDispatcher(alertQueue);
        dispatcherExecutor.submit(dispatcher);

        // 7. Shutdown hook for graceful termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down ClinicalAlertSystem...");

            // Stop sensors
            sensorExecutor.shutdownNow();

            // Stop dispatcher gracefully
            dispatcher.shutdown();
            dispatcherExecutor.shutdownNow();
        }));

        System.out.println("ClinicalAlertSystem started. Press Ctrl+C to stop.");
    }
}
