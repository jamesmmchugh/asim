package com.tolk.asim;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.tolk.asim.fxgui.IntroScene;

public class App extends Application {

    @Override
    public void start(final Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                primaryStage.close();
                Stage mainStage = new MainStage();
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 600, 250);

        root.resize(300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class MainStage extends Stage {
        public MainStage() {
            setTitle("Test");
            setScene(new IntroScene());
            show();
        }
    }
}
