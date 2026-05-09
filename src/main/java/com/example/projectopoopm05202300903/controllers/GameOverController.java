package com.example.projectopoopm05202300903.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOverController {
    @FXML private Label resultTitleLabel;
    @FXML private Label resultMessageLabel;
    @FXML private Label winnerLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label aiHpLabel;

    private boolean playerWon;

    public void setResult(String winnerName, boolean playerWon, int playerHp, int aiHp) {
        this.playerWon = playerWon;

        if (playerWon) {
            resultTitleLabel.setText("VITÓRIA!");
            resultTitleLabel.setTextFill(javafx.scene.paint.Color.web("#51cf66"));
            resultMessageLabel.setText("Parabéns, " + winnerName + "! Venceste o duelo!");
        } else {
            resultTitleLabel.setText("DERROTA!");
            resultTitleLabel.setTextFill(javafx.scene.paint.Color.web("#ff6b6b"));
            resultMessageLabel.setText("O PC venceu desta vez...");
        }

        winnerLabel.setText("Vencedor: " + winnerName);
        playerHpLabel.setText("HP Jogador: " + playerHp);
        aiHpLabel.setText("HP PC: " + aiHp);
    }

    @FXML
    private void handlePlayAgain() {
        changeScene("/com/example/projectopoopm05202300903/views/game-arena.fxml");
    }

    @FXML
    private void handleBackToMenu() {
        changeScene("/com/example/projectopoopm05202300903/views/main-menu.fxml");
    }

    private void changeScene(String fxmlPath) {
        try {
            Stage stage = (Stage) resultTitleLabel.getScene().getWindow();

            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();

            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root, currentWidth, currentHeight);

            scene.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case F11 -> {
                        stage.setFullScreenExitHint("");
                        stage.setFullScreen(!stage.isFullScreen());
                    }
                    case ESCAPE -> {
                        if (stage.isFullScreen()) {
                            stage.setFullScreen(false);
                        }
                    }
                }
            });

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
            System.out.println("Erro ao mudar de página: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
