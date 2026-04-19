package com.example.projectopoopm05202300903;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    protected void onCreateNewGameClick(ActionEvent event) {
        System.out.println("Creating New Game...");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-arena.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1080, 720);

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
