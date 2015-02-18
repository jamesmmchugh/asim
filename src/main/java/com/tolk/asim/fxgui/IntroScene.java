package com.tolk.asim.fxgui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class IntroScene extends Scene {
    public IntroScene() {
        super(new IntroPane(), 600, 600);
    }

    private static class IntroPane extends StackPane {
        public IntroPane() {
            Button btn = new Button();
            btn.setText("Say 'Hello World'");
            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                }
            });

            getChildren().add(btn);
        }
    }
}
