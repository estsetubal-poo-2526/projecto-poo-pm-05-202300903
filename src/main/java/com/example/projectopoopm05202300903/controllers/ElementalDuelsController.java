package com.example.projectopoopm05202300903.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElementalDuelsController {
    @FXML
    protected void onCreateNewGameClick(ActionEvent event) {
        System.out.println("Creating New Game...");

        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(ElementalDuelsController.class.getResource("/com/example/projectopoopm05202300903/views/game-arena.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage.setTitle("Elemental Duels - Arena");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading GameArena.fxml: " + e.getMessage());
        }
    }


    @FXML
    protected void onExitGameClick(ActionEvent event) {
        Platform.exit();
    }
}
