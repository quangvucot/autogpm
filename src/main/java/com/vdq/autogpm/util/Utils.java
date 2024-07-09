package com.vdq.autogpm.util;

import com.vdq.autogpm.ui.MainApp;

import java.io.File;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;

public class Utils {
    private static final Logger logger = Logger.getLogger(MainApp.class.getName());

    public static void writelog(String log) {
        String logDir = "C:/ProgramData/MySoftware/logs";
        File directory = new File(logDir);

        // Tạo thư mục nếu chưa tồn tại
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String logFilePath = logDir + "/myapp.log";
        try {
            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.log(Level.SEVERE, "An error occurred: ", log);
        } catch (Exception e) {

        }
    }

    public static double randomValue(double start, double end) {
        Random random = new Random();
        double randomValue = start + (end * random.nextDouble());
        randomValue = Math.round(randomValue * 10.0) / 10.0;
        return randomValue;
    }
}
