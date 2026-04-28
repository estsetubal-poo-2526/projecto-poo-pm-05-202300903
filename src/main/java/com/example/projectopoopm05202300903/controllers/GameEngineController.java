package com.example.projectopoopm05202300903.controllers;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.PlayerType;
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

    @FXML private HBox aiBoardBox;
    @FXML private HBox playerBoardBox;

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label playerManaLabel;
    @FXML private HBox  playerHandBox;
    @FXML private Label playerDeckCountLabel;

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
        } catch (EmptyDeckException ex) {
            logMessage("Erro ao iniciar o jogo: " + ex.getMessage());
        }
        updateUI();
    }

    private void updateUI() {
        var humanPlayer = engine.getHumanPlayer();
        var aiPlayer    = engine.getAiPlayer();

        playerNameLabel.setText(humanPlayer.getName());
        playerHpLabel.setText("HP: " + humanPlayer.getCurrentHp() + "/" + humanPlayer.getMaxHp());
        playerManaLabel.setText("Mana: " + humanPlayer.getCurrentMana() + "/" + humanPlayer.getMaxMana());
        playerDeckCountLabel.setText("Deck: " + humanPlayer.getDeck().size());

        aiNameLabel.setText(aiPlayer.getName());
        aiHpLabel.setText("HP: " + aiPlayer.getCurrentHp() + "/" + aiPlayer.getMaxHp());
        aiManaLabel.setText("Mana: " + aiPlayer.getCurrentMana() + "/" + aiPlayer.getMaxMana());
        aiDeckCountLabel.setText("Deck: " + aiPlayer.getDeck().size());

        renderHand();
        renderBoards();

        endTurnButton.setDisable(engine.isGameOver() || !engine.isHumanTurn());

        if (engine.isGameOver()) showGameOverAlert();
    }

    private void renderHand() {
        playerHandBox.getChildren().clear();
        for (Card card : engine.getHumanPlayer().getHand().getCards()) {
            CardView cardView = new CardView(card, false, false);
            boolean canAfford = card.getManaCost() <= engine.getHumanPlayer().getCurrentMana();
            if (!canAfford) cardView.setOpacity(0.45);
            cardView.setOnMouseClicked(event -> onHandCardClicked(card));
            playerHandBox.getChildren().add(cardView);
        }

        aiHandBox.getChildren().clear();
        for (Card aiCard : engine.getAiPlayer().getHand().getCards()) {
            CardView cardView = new CardView(aiCard, true, false);
            cardView.setCursor(javafx.scene.Cursor.DEFAULT);
            aiHandBox.getChildren().add(cardView);
        }
    }

    private void renderBoards() {
        playerBoardBox.getChildren().clear();
        for (UnitCard unit : engine.getBoard().getUnits(PlayerType.PLAYER)) {
            CardView cardView = new CardView(unit, false, true);
            if (unit.hasAttackedThisTurn()) cardView.setOpacity(0.6);
            cardView.setOnMouseClicked(event -> onPlayerBoardUnitClicked(cardView, unit));
            playerBoardBox.getChildren().add(cardView);
        }

        aiBoardBox.getChildren().clear();
        for (UnitCard unit : engine.getBoard().getUnits(PlayerType.AI)) {
            CardView cardView = new CardView(unit, false, true);
            cardView.setOnMouseClicked(event -> onEnemyUnitClicked(unit));
            aiBoardBox.getChildren().add(cardView);
        }

        aiHpLabel.setOnMouseClicked(event -> onAttackAiPlayerDirect());
        aiHpLabel.setCursor(selectedBoardUnit != null
                ? javafx.scene.Cursor.HAND : javafx.scene.Cursor.DEFAULT);
    }

    private void onHandCardClicked(Card card) {
        if (!engine.isHumanTurn() || engine.isGameOver()) return;
        clearBoardSelection();
        try {
            engine.playCardFromHand(card);
            updateUI();
        } catch (InsufficientManaException ex) {
            logMessage("Mana insuficiente!");
        }
    }

    private void onPlayerBoardUnitClicked(CardView cardView, UnitCard unit) {
        if (!engine.isHumanTurn() || engine.isGameOver()) return;
        if (unit.hasAttackedThisTurn()) {
            logMessage(unit.getName() + " já atacou este turno.");
            return;
        }
        if (selectedBoardView != null) selectedBoardView.setSelected(false);

        if (selectedBoardView == cardView) {
            selectedBoardUnit = null;
            selectedBoardView = null;
        } else {
            selectedBoardUnit = unit;
            selectedBoardView = cardView;
            cardView.setSelected(true);
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
        if (!engine.getBoard().getUnits(PlayerType.AI).isEmpty()) {
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

    private void logMessage(String message) {
        Platform.runLater(() -> {
            combatLogArea.appendText(message + "\n");
            combatLogArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void showGameOverAlert() {
        Platform.runLater(() -> {
            Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            gameOverAlert.setTitle("Fim de Jogo");
            gameOverAlert.setHeaderText(null);
            String winnerName = engine.getWinnerName();
            if ("Jogador".equals(winnerName)) {
                gameOverAlert.setContentText("VITÓRIA! Parabéns, " + winnerName + "!");
            } else {
                gameOverAlert.setContentText("DERROTA! O PC venceu desta vez...");
            }
            gameOverAlert.showAndWait();
        });
    }
}