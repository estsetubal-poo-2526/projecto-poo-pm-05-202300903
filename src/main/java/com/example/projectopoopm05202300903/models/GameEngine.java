package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.interfaces.ITarget;
import com.example.projectopoopm05202300903.models.Card.UnitCard;
import com.example.projectopoopm05202300903.models.Player.AiPlayer;
import com.example.projectopoopm05202300903.models.Player.HumanPlayer;
import com.example.projectopoopm05202300903.models.Player.Player;

import java.util.List;

public class GameEngine {
    private Board board;
    private HumanPlayer human;
    private AiPlayer pc;
    private Player currentPlayer;
    private int turnNumber;
    private boolean isGameOver;

    public GameEngine(HumanPlayer human, AiPlayer pc) {
        this.human = human;
        this.pc = pc;
        this.board = new Board();
        this.currentPlayer = human; // TODO: This should be random, if possible that the "AI" might start firsts
        this.turnNumber = 0;
        this.isGameOver = false;
    }

    public void startGame() {
        // TODO: Implement the game start
    }

    public void endCurrentTurn() {
        // TODO: Implement the end game
    }

    public void checkWinConditions() {
        // TODO: Check if it's possible to simplify this function, or even this function is necessary
//        if (!human.isAlive()) {
//            isGameOver = true;
//        }
//        if (!pc.isAlive()) {
//            isGameOver = true;
//        }
    }

    public void processAttack(UnitCard attacker, ITarget target) {
        // TODO: Implement the attack process
    }

    private void resetUnitsAttackStatus(List<UnitCard> units) {
//        for (UnitCard unit : units) {
//            unit.resetAttackStatus();
//        }
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
        // TODO: This function should be used inside the game initial loop
        if (!isGameOver) return null;
        if (!human.isAlive()) return pc;
        if (!pc.isAlive()) return human;
        return null;
    }

}
