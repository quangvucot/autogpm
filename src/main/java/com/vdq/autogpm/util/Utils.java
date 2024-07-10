package com.vdq.autogpm.util;

import com.vdq.autogpm.ui.MainApp;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

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
    public static void showInfoAlert(String title, String headerText, String contentText) {
        showAlert(Alert.AlertType.INFORMATION, title, headerText, contentText);
    }

    public static void showWarningAlert(String title, String headerText, String contentText) {
        showAlert(Alert.AlertType.WARNING, title, headerText, contentText);
    }

    public static void showErrorAlert(String title, String headerText, String contentText) {
        showAlert(Alert.AlertType.ERROR, title, headerText, contentText);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
