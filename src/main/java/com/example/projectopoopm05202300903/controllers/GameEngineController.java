package com.example.projectopoopm05202300903.controllers;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.GameEngine;
import com.example.projectopoopm05202300903.views.CardView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class GameEngineController {

    private GameEngine engine;

    @FXML private Label aiNameLabel;
    @FXML private Label aiHpLabel;
    @FXML private Label aiManaLabel;
    @FXML private HBox  aiHandBox;
    @FXML private Label aiDeckCountLabel;
    @FXML private Label aiTimerLabel;

    @FXML private HBox aiBoardBox;
    @FXML private HBox playerBoardBox;

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label playerManaLabel;
    @FXML private HBox  playerHandBox;
    @FXML private Label playerDeckCountLabel;
    @FXML private Label playerTimerLabel;

    @FXML private TextArea combatLogArea;
    @FXML private Button   endTurnButton;

    private CardView  selectedHandView  = null;
    private UnitCard  selectedBoardUnit = null;
    private CardView  selectedBoardView = null;

    @FXML
    public void initialize() {
        engine = new GameEngine("Jogador", this::logMessage);
        try {
            engine.startGame();
        } catch (EmptyDeckException e) {
            logMessage("Erro ao iniciar o jogo: " + e.getMessage());
        }
        updateUI();
    }

    private void updateUI() {
        var hp = engine.getHumanPlayer();
        var ai = engine.getAiPlayer();

        playerNameLabel.setText(hp.getName());
        playerHpLabel.setText("HP: " + hp.getCurrentHp() + "/" + hp.getMaxHp());
        playerManaLabel.setText("Mana: " + hp.getCurrentMana() + "/" + hp.getMaxMana());
        playerDeckCountLabel.setText("Deck: " + hp.getDeck().size());

        aiNameLabel.setText(ai.getName());
        aiHpLabel.setText("HP: " + ai.getCurrentHp() + "/" + ai.getMaxHp());
        aiManaLabel.setText("Mana: " + ai.getCurrentMana() + "/" + ai.getMaxMana());
        aiDeckCountLabel.setText("Deck: " + ai.getDeck().size());

        renderHand();
        renderBoards();

        endTurnButton.setDisable(engine.isGameOver() || engine.isHumanTurn());

        if (engine.isGameOver()) showGameOverAlert();
    }

    private void renderHand() {
        playerHandBox.getChildren().clear();
        for (Card card : engine.getHumanPlayer().getHand().getCards()) {
            CardView cv = new CardView(card, false, false);
            boolean affordable = card.getManaCost() <= engine.getHumanPlayer().getCurrentMana();
            if (!affordable) cv.setOpacity(0.45);
            cv.setOnMouseClicked(e -> onHandCardClicked(card));
            playerHandBox.getChildren().add(cv);
        }

        aiHandBox.getChildren().clear();
        for (Card ignored : engine.getAiPlayer().getHand().getCards()) {
            CardView cv = new CardView(ignored, true, false);
            cv.setCursor(javafx.scene.Cursor.DEFAULT);
            aiHandBox.getChildren().add(cv);
        }
    }

    private void renderBoards() {
        playerBoardBox.getChildren().clear();
        for (UnitCard unit : engine.getBoard().getPlayerUnits()) {
            CardView cv = new CardView(unit, false, true);
            if (unit.hasAttackedThisTurn()) cv.setOpacity(0.6);
            cv.setOnMouseClicked(e -> onPlayerBoardUnitClicked(cv, unit));
            playerBoardBox.getChildren().add(cv);
        }

        aiBoardBox.getChildren().clear();
        for (UnitCard unit : engine.getBoard().getPcUnits()) {
            CardView cv = new CardView(unit, false, true);
            cv.setOnMouseClicked(e -> onEnemyUnitClicked(unit));
            aiBoardBox.getChildren().add(cv);
        }

        aiHpLabel.setOnMouseClicked(e -> onAttackAiPlayerDirect());
        aiHpLabel.setCursor(selectedBoardUnit != null
                ? javafx.scene.Cursor.HAND : javafx.scene.Cursor.DEFAULT);
    }

    private void onHandCardClicked(Card card) {
        if (engine.isHumanTurn() || engine.isGameOver()) return;
        clearBoardSelection();
        try {
            engine.playCardFromHand(card);
            updateUI();
        } catch (InsufficientManaException e) {
            logMessage("Mana insuficiente!");
        }
    }

    private void onPlayerBoardUnitClicked(CardView cv, UnitCard unit) {
        if (engine.isHumanTurn() || engine.isGameOver()) return;
        if (unit.hasAttackedThisTurn()) {
            logMessage(unit.getName() + " já atacou este turno.");
            return;
        }
        if (selectedBoardView != null) selectedBoardView.setSelected(false);

        if (selectedBoardView == cv) {
            selectedBoardUnit = null;
            selectedBoardView = null;
        } else {
            selectedBoardUnit = unit;
            selectedBoardView = cv;
            cv.setSelected(true);
            logMessage("Seleccionado: " + unit.getName() + ". Clique num inimigo para atacar.");
        }
        renderBoards();
    }

    private void onEnemyUnitClicked(UnitCard target) {
        if (selectedBoardUnit == null || engine.isGameOver()) return;
        engine.processAttack(selectedBoardUnit, target);
        clearBoardSelection();
        updateUI();
    }

    private void onAttackAiPlayerDirect() {
        if (selectedBoardUnit == null || engine.isGameOver()) return;
        if (!engine.getBoard().getPcUnits().isEmpty()) {
            logMessage("Não pode atacar o PC directamente enquanto tiver unidades no campo!");
            return;
        }
        engine.processAttack(selectedBoardUnit, engine.getAiPlayer());
        clearBoardSelection();
        updateUI();
    }

    @FXML
    private void handleEndTurn() {
        clearBoardSelection();
        engine.endHumanTurn();
        updateUI();
    }

    private void clearBoardSelection() {
        if (selectedBoardView != null) selectedBoardView.setSelected(false);
        selectedBoardUnit = null;
        selectedBoardView = null;
        selectedHandView  = null;
    }

    private void logMessage(String msg) {
        Platform.runLater(() -> {
            combatLogArea.appendText(msg + "\n");
            combatLogArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void showGameOverAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fim de Jogo");
            alert.setHeaderText(null);
            String winner = engine.getWinnerName();
            if ("Jogador".equals(winner)) {
                alert.setContentText("VITÓRIA! Parabéns, " + winner + "!");
            } else {
                alert.setContentText("DERROTA! O PC venceu desta vez...");
            }
            alert.showAndWait();
        });
    }
}