package com.example.projectopoopm05202300903.controllers;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.PlayerType;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.GameEngine;
import com.example.projectopoopm05202300903.views.CardView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

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

    @FXML private StackPane pauseOverlay;

    private CardView  selectedHandView  = null;
    private UnitCard  selectedBoardUnit = null;
    private CardView  selectedBoardView = null;

    private Timeline turnTimer;
    private int timeLeft = 30;

    private boolean gameOverShown = false;
    private boolean paused = false;

    @FXML
    public void initialize() {
        engine = new GameEngine("Jogador", this::logMessage);
        try {
            engine.startGame();
        } catch (EmptyDeckException ex) {
            logMessage("Erro ao iniciar o jogo: " + ex.getMessage());
        }
        updateUI();
        startTurnTimer();
        setupPauseMenuShortcut();
    }

    private void updateUI() {
        var humanPlayer = engine.getHumanPlayer();
        var aiPlayer    = engine.getAiPlayer();

        playerNameLabel.setText(humanPlayer.getName());
        playerHpLabel.setText("HP: " + humanPlayer.getCurrentHp() + "/" + humanPlayer.getMaxHp());
        playerManaLabel.setText("Mana: " + humanPlayer.getCurrentMana() + "/" + humanPlayer.getManaLimit());
        playerDeckCountLabel.setText("Deck: " + humanPlayer.getDeck().size());

        aiNameLabel.setText(aiPlayer.getName());
        aiHpLabel.setText("HP: " + aiPlayer.getCurrentHp() + "/" + aiPlayer.getMaxHp());
        aiManaLabel.setText("Mana: " + aiPlayer.getCurrentMana() + "/" + aiPlayer.getManaLimit());
        aiDeckCountLabel.setText("Deck: " + aiPlayer.getDeck().size());

        renderHand();
        renderBoards();

        endTurnButton.setDisable(engine.isGameOver() || !engine.isHumanTurn());

        if (engine.isGameOver() && !gameOverShown) {
            gameOverShown = true;
            showGameOverPage();
        }
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
        if (paused || !engine.isHumanTurn() || engine.isGameOver()) return;
        clearBoardSelection();
        try {
            engine.playCardFromHand(card);
            updateUI();
        } catch (InsufficientManaException _) {
            logMessage("Mana insuficiente!");
        }
    }

    private void onPlayerBoardUnitClicked(CardView cardView, UnitCard unit) {
        if (paused || !engine.isHumanTurn() || engine.isGameOver()) return;
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
        if (paused || selectedBoardUnit == null || engine.isGameOver()) return;
        engine.processAttack(selectedBoardUnit, target);
        clearBoardSelection();
        updateUI();
    }

    private void onAttackAiPlayerDirect() {
        if (paused || selectedBoardUnit == null || engine.isGameOver()) return;
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
        if (paused) return;

        clearBoardSelection();
        engine.endHumanTurn();
        updateUI();

        if (!engine.isGameOver()) {
            startTurnTimer();
        } else if (turnTimer != null) {
            turnTimer.stop();
        }
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

    private void showGameOverPage() {
        Platform.runLater(() -> {
            try {
                var fxmlUrl = getClass().getResource(
                        "/com/example/projectopoopm05202300903/views/game-over.fxml"
                );

                if (fxmlUrl == null) {
                    logMessage("Erro: ficheiro game-over-view.fxml não encontrado.");
                    return;
                }

                Stage stage = (Stage) playerNameLabel.getScene().getWindow();

                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();

                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                GameOverController controller = loader.getController();

                String winnerName = engine.getWinnerName();
                boolean playerWon = engine.getHumanPlayer().getName().equals(winnerName);

                controller.setResult(
                        winnerName,
                        playerWon,
                        engine.getHumanPlayer().getCurrentHp(),
                        engine.getAiPlayer().getCurrentHp()
                );

                Scene scene = new Scene(root, currentWidth, currentHeight);

                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.F11) {
                        stage.setFullScreenExitHint("");
                        stage.setFullScreen(!stage.isFullScreen());
                    }
                    else if (event.getCode() == KeyCode.ESCAPE) {
                        if (stage.isFullScreen()) {
                            stage.setFullScreen(false);
                        }
                    }
                });

                stage.setTitle("Elemental Duels - Fim de Jogo");
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
                logMessage("Erro ao abrir página de fim de jogo: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void startTurnTimer() {
        if (turnTimer != null) {
            turnTimer.stop();
        }

        timeLeft = 30;
        updateTimerLabels();

        turnTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft--;
            updateTimerLabels();

            if (timeLeft <= 0) {
                turnTimer.stop();

                if (engine.isHumanTurn() && !engine.isGameOver()) {
                    logMessage("Tempo esgotado! O teu turno terminou automaticamente.");
                    handleEndTurn();
                }
            }
        }));

        turnTimer.setCycleCount(Timeline.INDEFINITE);
        turnTimer.play();
    }

    private void updateTimerLabels() {
        if (engine.isHumanTurn()) {
            playerTimerLabel.setText("Tempo: " + timeLeft + "s");
            aiTimerLabel.setText("Tempo: -");
        } else {
            aiTimerLabel.setText("Tempo: " + timeLeft + "s");
            playerTimerLabel.setText("Tempo: -");
        }
    }

    private void setupPauseMenuShortcut() {
        Platform.runLater(() -> {
            Scene scene = playerNameLabel.getScene();

            if (scene == null) return;

            Stage stage = (Stage) scene.getWindow();

            // Isto impede o JavaFX de usar ESC para sair automaticamente do fullscreen.
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreenExitHint("");

            scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    togglePauseMenu();
                    event.consume();
                }

                if (event.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                    stage.setFullScreenExitHint("");
                    event.consume();
                }
            });
        });
    }

    private void togglePauseMenu() {
        if (engine.isGameOver()) return;

        if (paused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        paused = true;

        pauseOverlay.setVisible(true);
        pauseOverlay.setManaged(true);
        pauseOverlay.toFront();

        if (turnTimer != null) {
            turnTimer.pause();
        }

        endTurnButton.setDisable(true);
    }

    private void resumeGame() {
        paused = false;

        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        if (turnTimer != null && !engine.isGameOver()) {
            turnTimer.play();
        }

        endTurnButton.setDisable(engine.isGameOver() || !engine.isHumanTurn());
    }

    @FXML
    private void handleResumeGame() {
        resumeGame();
    }

    @FXML
    private void handleRestartGame() {
        changeScene("/com/example/projectopoopm05202300903/views/game-arena.fxml");
    }

    @FXML
    private void handleExitApplication() {
        Platform.exit();
    }

    private void changeScene(String fxmlPath) {
        try {
            Stage stage = (Stage) playerNameLabel.getScene().getWindow();

            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();

            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root, currentWidth, currentHeight);

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.F11) {
                    stage.setFullScreenExitHint("");
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });

            stage.setScene(scene);
            stage.show();

            if (wasFullScreen) {
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
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