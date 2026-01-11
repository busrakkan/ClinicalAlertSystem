package com.clinicalalertsystem.logging;

import java.io.InputStream;
import java.util.logging.Logger;

public final class LogManager {

    static {
        try (InputStream config =
                     LogManager.class
                             .getClassLoader()
                             .getResourceAsStream("logging.properties")) {

            if (config != null) {
                java.util.logging.LogManager.getLogManager().readConfiguration(config);
            } else {
                System.err.println("logging.properties not found, using defaults");
            }

        } catch (Exception e) {
            System.err.println("Failed to load logging configuration: " + e.getMessage());
        }
    }

    private LogManager() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}