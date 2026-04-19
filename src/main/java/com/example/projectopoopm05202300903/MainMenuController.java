package com.example.projectopoopm05202300903;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuController {

    // TODO: Implement Code and JavaDoc
    @FXML
    protected void onCreateNewGameClick(ActionEvent event) {
        System.out.println("Creating New Game");
    }


    // TODO: Implement JavaDoc
    @FXML
    protected void onExitGameClick(ActionEvent event) {
        Platform.exit();
    }
}
