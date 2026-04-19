package com.example.projectopoopm05202300903.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ElementalDuelsController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
