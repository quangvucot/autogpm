package com.vdq.autogpm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainApp extends Application {
	private static final Logger logger = Logger.getLogger(MainApp.class.getName());
	@Override
	public void start(Stage stage) throws IOException {
		try{
			LogManager.getLogManager().readConfiguration(MainApp.class.getResourceAsStream("/logging.properties"));
			logger.info("Application started");
			FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/vdq/autogpm/main.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 1000,1000);
			stage.setTitle("Vu Quang!");
			stage.setScene(scene);
			stage.show();

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch();
	}


}
