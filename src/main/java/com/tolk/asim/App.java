package com.tolk.asim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {

	@Override
	public void start(final Stage primaryStage) {
		Pane introPane;
		try {
			introPane = FXMLLoader.load(getClass().getResource("introScene.fxml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Scene scene = new Scene(introPane, 600, 250);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// IntroStage undecorated, timer until transition to MainStage
	// MainStage, simulation and quick controls
}
