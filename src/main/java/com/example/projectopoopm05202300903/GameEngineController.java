package com.example.projectopoopm05202300903;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class GameEngineController {
    //private GameEngine engine;

    // TOP AREA: AI Player (Opponent)
    @FXML
    private Label aiNameLabel;
    @FXML
    private Label aiHpLabel;
    @FXML
    private Label aiManaLabel;
    @FXML
    private HBox aiHandBox;
    @FXML
    private Label aiDeckCountLabel;
    @FXML
    private Label aiTimerLabel;

    // CENTER AREA: Battlefield
    @FXML
    private HBox aiBoardBox;
    @FXML
    private HBox playerBoardBox;

    // BOTTOM AREA: Human Player
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label playerHpLabel;
    @FXML
    private Label playerManaLabel;
    @FXML
    private HBox playerHandBox;
    @FXML
    private Label playerDeckCountLabel;
    @FXML
    private Label playerTimerLabel;

    // RIGHT AREA: Logs & Controls
    @FXML
    private TextArea combatLogArea;
    @FXML
    private Button endTurnButton;

    // TODO: Update UI after turn
    private void updateUI() {

    }

    @FXML
    private void handleEndTurn() {
        //engine.endCurrentTurn();
        //engine.checkWinConditions();
    }
}
