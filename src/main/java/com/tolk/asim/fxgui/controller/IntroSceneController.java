package com.tolk.asim.fxgui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class IntroSceneController implements Initializable {

    public static final String APP_NAME = "asim";
    public static final String APP_DESCRIPTION = "An artificial life simulation.";
    public static final String APP_AUTHOR = "By James Marc McHugh";

    @FXML private Label appNameLabel;
    @FXML private Label appDescriptionLabel;
    @FXML private Label appAuthorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appNameLabel.setText(APP_NAME);
        appDescriptionLabel.setText(APP_DESCRIPTION);
        appAuthorLabel.setText(APP_AUTHOR);
    }
}
