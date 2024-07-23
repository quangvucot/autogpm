package com.vdq.autogpm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {

    private static final String LOG_DIR = "logs"; // Thư mục lưu các file log

    // Sử dụng ThreadLocal để lưu Logger riêng biệt cho mỗi luồng
    private static final ThreadLocal<Logger> threadLocalLogger = new ThreadLocal<>();

    // Phương thức để khởi tạo Logger với tên file log
    public static void initLogger(String logFileName) {
        Logger logger = Logger.getLogger(Thread.currentThread().getName());
        try {
            // Tạo thư mục nếu chưa tồn tại
            Path logDirPath = Paths.get(LOG_DIR);
            if (!Files.exists(logDirPath)) {
                Files.createDirectories(logDirPath);
            }
            // Tạo FileHandler với đường dẫn tới thư mục log và tên file log
            FileHandler fileHandler = new FileHandler(LOG_DIR + "/" + logFileName + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadLocalLogger.set(logger);
    }

    public static Logger getLogger() {
        return threadLocalLogger.get();
    }

    public static void log(String message) {
        Logger logger = getLogger();
        logger.log(Level.INFO, message);
    }

    public static void logError(String message, Exception e) {
        Logger logger = getLogger();
        logger.log(Level.SEVERE, message, e);
    }
}
