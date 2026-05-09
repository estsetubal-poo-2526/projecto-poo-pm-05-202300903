package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.CardFactory;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.PlayerType;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;
import com.example.projectopoopm05202300903.models.player.AiPlayer;
import com.example.projectopoopm05202300903.models.player.HumanPlayer;
import com.example.projectopoopm05202300903.models.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.function.Consumer;

public class GameEngine {

    private final Player   humanPlayer;
    private final AiPlayer aiPlayer;
    private final Board    board;

    private boolean humanTurn  = true;
    private int     turnNumber = 1;
    private boolean isGameOver = false;
    private String  winnerName = null;

    private final Consumer<String> logCallback;


    public GameEngine(String playerName, Consumer<String> logCallback) {
        this.humanPlayer = new HumanPlayer(playerName);
        this.aiPlayer    = new AiPlayer("AI - Oponente");
        this.board       = new Board();
        this.logCallback = logCallback != null ? logCallback : _ -> {};
    }

    public void startGame() throws EmptyDeckException {
        List<Card> deck1 = CardFactory.createDeck();
        List<Card> deck2 = CardFactory.createDeck();
        deck1.forEach(humanPlayer.getDeck()::addCard);
        deck2.forEach(aiPlayer.getDeck()::addCard);
        humanPlayer.getDeck().shuffle();
        aiPlayer.getDeck().shuffle();

        for (int i = 0; i < 3; i++) {
            humanPlayer.getHand().addCard(humanPlayer.getDeck().drawCard());
            aiPlayer.getHand().addCard(aiPlayer.getDeck().drawCard());
        }

        humanPlayer.startTurn();
        log("Jogo iniciado! \nTurno 1 — " + humanPlayer.getName() + " começa.");
    }

    public void playCardFromHand(Card card) throws InsufficientManaException {
        humanPlayer.playCard(card);
        log("Você " + card.play(humanPlayer, aiPlayer, board));
        checkWinConditions();
    }

    public void processAttack(UnitCard attacker, ITarget target) {
        if (attacker.hasAttackedThisTurn() || isGameOver) return;

        int dmg = attacker.getAttack();
        attacker.attackTarget(target);
        log(attacker.getName() + " atacou e causou " + dmg + " de dano!");

        board.removeDeadUnits();
        checkWinConditions();
    }

    public void endHumanTurn() {
        if (!humanTurn || isGameOver) return;
        log("Fim do teu turno.\n");

        board.getUnits(PlayerType.PLAYER).forEach(UnitCard::resetAttackStatus);
        humanTurn = false;
        turnNumber++;

        try {
            processAiTurn();
        } catch (EmptyDeckException _) {log("O baralho do oponente esgotou! Vitória tua!");
            isGameOver = true;
            winnerName = humanPlayer.getName();
        }

        if (!isGameOver) {
            try {
                humanPlayer.startTurn();
            } catch (EmptyDeckException _) {
                log("O teu baralho esgotou! Derrota!");
                isGameOver = true;
                winnerName = aiPlayer.getName();
                return;
            }
            humanTurn = true;
            log("\nTurno " + turnNumber + " — " + humanPlayer.getName() + ".");
        }
    }

    private void processAiTurn() throws EmptyDeckException {
        aiPlayer.startTurn();
        board.getUnits(PlayerType.AI).forEach(UnitCard::resetAttackStatus);
        log("[Oponente] A jogar...");

        playAiCards();
        if (!isGameOver) performAiAttacks();
    }

    private void playAiCards() {
        List<Card> hand = new ArrayList<>(aiPlayer.getHand().getCards());
        hand.sort(Comparator.comparingInt(Card::getManaCost));

        for (Card card : hand) {
            if (card.getManaCost() > aiPlayer.getCurrentMana()) continue;
            try {
                aiPlayer.playCard(card);
                log("[Oponente] " + card.play(aiPlayer, humanPlayer, board));
                checkWinConditions();
                if (isGameOver) return;
            } catch (InsufficientManaException _) {  }
        }
    }

    private void performAiAttacks() {
        for (UnitCard attacker : new ArrayList<>(board.getUnits(PlayerType.AI))) {
            if (attacker.isDead() || attacker.hasAttackedThisTurn()) continue;

            List<UnitCard> targets = board.getUnits(PlayerType.PLAYER);
            if (!targets.isEmpty()) {
                UnitCard target = targets.getFirst();
                attacker.attackTarget(target);
                log("[Oponente] " + attacker.getName() + " atacou " + target.getName() + " e causou " + attacker.getAttack() + " de dano!");
            } else {
                attacker.attackTarget(humanPlayer);
                log("[Oponente] " + attacker.getName() + " atacou-te diretamente por "
                        + attacker.getAttack() + " de dano!");
            }

            board.removeDeadUnits();
            checkWinConditions();
            if (isGameOver) return;
        }
    }

    private void checkWinConditions() {
        if (humanPlayer.isDead()) {
            isGameOver = true;
            winnerName = aiPlayer.getName();
            log("DERROTA! " + aiPlayer.getName() + " venceu o duelo.");
        } else if (aiPlayer.isDead()) {
            isGameOver = true;
            winnerName = humanPlayer.getName();
            log("VITÓRIA! " + humanPlayer.getName() + " venceu o duelo!");
        }
    }

    private void log(String msg) {
        logCallback.accept(msg);
    }

    public Player  getHumanPlayer() { return humanPlayer; }
    public Player  getAiPlayer()    { return aiPlayer; }
    public Board   getBoard()       { return board; }
    public boolean isHumanTurn()    { return humanTurn; }
    public boolean isGameOver()     { return isGameOver; }
    public String  getWinnerName()  { return winnerName; }
}