package com.vdq.autogpm.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainApp extends Application {
	private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
	@Override
	public void start(Stage stage) throws IOException {
		try{
			FileHandler fh = new FileHandler("app.log");
			LOGGER.addHandler(fh);

			FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/vdq/autogpm/main.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 1000,1000);
			stage.setTitle("Vu Quang!");
			stage.setScene(scene);
			stage.show();
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			LOGGER.info("Application starting...");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch();
	}


}
