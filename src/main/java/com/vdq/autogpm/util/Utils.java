package com.vdq.autogpm.util;

import com.vdq.autogpm.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;

public class Utils {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String CONFIG_FILE = "config.properties";

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

    public static void log(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] " + message);
    }

    public static void logError(String message, Exception e) {
        String threadName = Thread.currentThread().getName();
        System.err.println("[" + threadName + "] " + message);
        e.printStackTrace();
    }

    private void saveTextFieldValue(TextField textField) {
        Properties properties = new Properties();
        properties.setProperty("textFieldValue", textField.getText());

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
            System.out.println("Configuration saved.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void loadTextFieldValue(TextField textField) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            String value = properties.getProperty("textFieldValue", "");
            textField.setText(value);
            System.out.println("Configuration loaded.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
