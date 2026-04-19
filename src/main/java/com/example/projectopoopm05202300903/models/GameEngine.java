package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.Exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.interfaces.ITarget;

import java.util.List;

public class GameEngine {
    private HumanPlayer human;
    private AiPlayer pc;
    private Board board;
    private Player currentPlayer;
    private int turnNumber;
    private boolean isGameOver;

    public GameEngine(HumanPlayer human, AiPlayer pc) {
        this.human = human;
        this.pc = pc;
        this.board = new Board();
        this.currentPlayer = human;
        this.turnNumber = 0;
        this.isGameOver = false;

        this.pc.setBoard(board);
    }

    public void startGame() {
        isGameOver = false;
        turnNumber = 0;

        human.getDeck().shuffle();
        pc.getDeck().shuffle();

        for (int i = 0; i < 3; i++) {
            try {
                human.getHand().addCard(human.getDeck().drawCard());
                pc.getHand().addCard(pc.getDeck().drawCard());
            } catch (EmptyDeckException e) {
                isGameOver = true;
                return;
            }
        }
        turnNumber = 1;
        currentPlayer = human;
        human.executeTurn();
    }

    public void endCurrentTurn() {
        if (isGameOver) return;
        board.removeDeadUnits();

        checkWinConditions();
        if (isGameOver) return;

        if (currentPlayer == human) {
            resetUnitsAttackStatus(board.getPlayerUnits());

            currentPlayer = pc;
            pc.executeTurn();

            board.removeDeadUnits();
            checkWinConditions();
            if (isGameOver) return;

            resetUnitsAttackStatus(board.getPcUnits());

            turnNumber++;
            currentPlayer = human;
            human.executeTurn();
        }
    }

    public void checkWinConditions() {
        if (!human.isAlive()) {
            isGameOver = true;
        }
        if (!pc.isAlive()) {
            isGameOver = true;
        }
    }

    public void processAttack(UnitCard attacker, ITarget target) {
        if (isGameOver) return;

        if (attacker.hasAttackedThisTurn() || !attacker.isAlive()) {
            return;
        }

        attacker.attackTarget(target);

        if (target instanceof UnitCard) {
            UnitCard targetUnit = (UnitCard) target;
            if (targetUnit.isAlive()) {
                attacker.receiveDamage(targetUnit.getAttack());
            }
        }

        board.removeDeadUnits();
        checkWinConditions();
    }

    private void resetUnitsAttackStatus(List<UnitCard> units) {
        for (UnitCard unit : units) {
            unit.resetAttackStatus();
        }
    }

    public HumanPlayer getHuman() {
        return human;
    }

    public AiPlayer getPc() {
        return pc;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isHumanTurn() {
        return currentPlayer == human;
    }

    public Player getWinner() {
        if (!isGameOver) return null;
        if (!human.isAlive()) return pc;
        if (!pc.isAlive()) return human;
        return null;
    }

}
