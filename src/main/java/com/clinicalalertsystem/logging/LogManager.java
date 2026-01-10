package com.clinicalalertsystem.logging;

import java.io.IOException;
import java.util.logging.*;

public class LogManager {

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);

        try {
            Handler fileHandler = new FileHandler("clinical-alert-system.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize logging: " + e.getMessage());
        }

        return logger;
    }
}
