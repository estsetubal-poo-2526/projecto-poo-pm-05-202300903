package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.CardFactory;
import com.example.projectopoopm05202300903.models.card.SpellCard;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.SpellType;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.player.AiPlayer;
import com.example.projectopoopm05202300903.models.player.HumanPlayer;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class GameEngine {
    private final HumanPlayer humanPlayer;
    private final AiPlayer aiPlayer;
    private final Board board;
    private boolean humanTurn  = true;
    private int turnNumber = 1;
    private boolean isGameOver = false;
    private String winnerName = null;
    private final Consumer<String> logCallback;

    public GameEngine(String playerName, Consumer<String> logCallback) {
        this.humanPlayer = new HumanPlayer(playerName);
        this.aiPlayer = new AiPlayer("PC - Oponente");
        this.board = new Board();
        this.logCallback = logCallback;
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

        humanPlayer.startTurnProcess();
        log("Jogo iniciado! Turno 1 — " + humanPlayer.getName() + ".");
    }

    public void playCardFromHand(Card card) throws InsufficientManaException {
        humanPlayer.playCard(card);
        if (card instanceof UnitCard unit) {
            board.addPlayerUnit(unit);
            log("Você jogou: " + card.getName() + ".");
        } else if (card instanceof SpellCard spell) {
            if (spell.getType() == SpellType.HEAL) {
                spell.applyEffect(humanPlayer);
                log("Você usou " + card.getName() + " e recuperou " + spell.getEffectValue() + " HP.");
            } else {
                spell.applyEffect(aiPlayer);
                log("Você usou " + card.getName() + " — PC sofreu " + spell.getEffectValue() + " de dano!");
            }
            checkWinConditions();
        }
    }

    public void processAttack(UnitCard attacker, ITarget target) {
        if (attacker.hasAttackedThisTurn() || isGameOver) return;

        String targetName = (target instanceof UnitCard u) ? u.getName() : aiPlayer.getName();
        int dmg = attacker.getAttack();
        attacker.attackTarget(target);
        log(attacker.getName() + " atacou " + targetName + " por " + dmg + " de dano!");

        board.removeDeadUnits();
        checkWinConditions();
    }

    public void endHumanTurn() {
        if (!humanTurn || isGameOver) return;
        log("-- Fim do seu turno --");

        board.getPlayerUnits().forEach(UnitCard::resetAttackStatus);
        humanTurn = false;
        turnNumber++;

        try {
            processAiTurn();
        } catch (EmptyDeckException _) {
            log("O baralho do PC está vazio! PC perde!");
            isGameOver = true;
            winnerName = humanPlayer.getName();
        }

        if (!isGameOver) {
            try {
                humanPlayer.startTurnProcess();
            } catch (EmptyDeckException _) {
                log("O seu baralho está vazio! Você perde!");
                isGameOver = true;
                winnerName = aiPlayer.getName();
                return;
            }
            humanTurn = true;
            log("-- Turno " + turnNumber + " — " + humanPlayer.getName() + " --");
        }
    }

    private void processAiTurn() throws EmptyDeckException {
        aiPlayer.startTurnProcess();
        log("[PC] Turno do oponente...");
        board.getPcUnits().forEach(UnitCard::resetAttackStatus);

        List<Card> hand = new ArrayList<>(aiPlayer.getHand().getCards());
        hand.sort(Comparator.comparingInt(Card::getManaCost));
        for (Card card : hand) {
            if (card.getManaCost() > aiPlayer.getCurrentMana()) continue;
            try {
                aiPlayer.playCard(card);
                if (card instanceof UnitCard unit) {
                    board.addPcUnit(unit);
                    log("[PC] jogou: " + card.getName() + ".");
                } else if (card instanceof SpellCard spell) {
                    if (spell.getType() == SpellType.DAMAGE) {
                        spell.applyEffect(humanPlayer);
                        log("[PC] usou " + card.getName() + " em você — " + spell.getEffectValue() + " de dano!");
                    } else {
                        spell.applyEffect(aiPlayer);
                        log("[PC] usou " + card.getName() + " e recuperou " + spell.getEffectValue() + " HP.");
                    }
                    checkWinConditions();
                    if (isGameOver) return;
                }
            } catch (InsufficientManaException _) {}
        }

        List<UnitCard> attackers = new ArrayList<>(board.getPcUnits());
        for (UnitCard attacker : attackers) {
            if (attacker.isDead() || attacker.hasAttackedThisTurn()) continue;
            List<UnitCard> targets = board.getPlayerUnits();
            if (!targets.isEmpty()) {
                UnitCard target = targets.get(0);
                attacker.attackTarget(target);
                log("[PC] " + attacker.getName() + " atacou " + target.getName() + ".");
            } else {
                attacker.attackTarget(humanPlayer);
                log("[PC] " + attacker.getName() + " atacou você por " + attacker.getAttack() + " de dano!");
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
            log("GAME OVER! O PC venceu!");
        } else if (aiPlayer.isDead()) {
            isGameOver = true;
            winnerName = humanPlayer.getName();
            log("VITÓRIA! Você venceu!");
        }
    }

    private void log(String msg) {
        if (logCallback != null) logCallback.accept(msg);
    }

    public HumanPlayer getHumanPlayer() { return humanPlayer; }
    public AiPlayer    getAiPlayer()    { return aiPlayer; }
    public Board       getBoard()       { return board; }
    public boolean     isHumanTurn()   { return !humanTurn; }
    public boolean     isGameOver()    { return isGameOver; }
    public String      getWinnerName() { return winnerName; }
}
