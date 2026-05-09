package com.example.projectopoopm05202300903.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElementalDuelsController {
    @FXML
    protected void onCreateNewGameClick(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();

            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            FXMLLoader fxmlLoader = new FXMLLoader(
                    ElementalDuelsController.class.getResource(
                            "/com/example/projectopoopm05202300903/views/game-arena.fxml"
                    )
            );

            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, currentWidth, currentHeight);

            scene.setOnKeyPressed(keyEvent -> {
                switch (keyEvent.getCode()) {
                    case F11 -> {
                        stage.setFullScreen(!stage.isFullScreen());
                        stage.setFullScreenExitHint("");
                    }
                    case ESCAPE -> {
                        if (stage.isFullScreen()) {
                            stage.setFullScreen(false);
                        }
                    }
                }
            });

            stage.setTitle("Elemental Duels - Arena");
            stage.setScene(scene);
            stage.show();

            if (wasFullScreen) {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            } else if (wasMaximized) {
                Platform.runLater(() -> {
                    stage.setMaximized(false);
                    stage.setMaximized(true);
                });
            }

        } catch (IOException e) {
            System.err.println("Error loading GameArena.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    protected void onExitGameClick(ActionEvent event) {
        Platform.exit();
    }
}
