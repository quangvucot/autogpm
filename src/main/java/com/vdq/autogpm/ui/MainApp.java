package com.vdq.autogpm.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/vdq/autogpm/main.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1000,1000);
		stage.setTitle("Vu Quang!");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
