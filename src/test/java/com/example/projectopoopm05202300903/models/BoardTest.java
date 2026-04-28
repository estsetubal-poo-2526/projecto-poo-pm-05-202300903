package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private UnitCard playerUnit;
    private UnitCard aiUnit;

    @BeforeEach
    void setUp() {
        board      = new Board();
        playerUnit = new UnitCard("PUnit", 1, "", 2, 4);
        aiUnit     = new UnitCard("AiUnit", 1, "", 3, 5);
    }

    @Test
    void addUnit_appearsInGetUnits() {
        board.addUnit(PlayerType.PLAYER, playerUnit);
        assertTrue(board.getUnits(PlayerType.PLAYER).contains(playerUnit));
    }

    @Test
    void addUnit_doesNotAppearInOpponentUnits() {
        board.addUnit(PlayerType.PLAYER, playerUnit);
        assertFalse(board.getUnits(PlayerType.AI).contains(playerUnit));
    }

    @Test
    void removeDeadUnits_removesDeadCard() {
        board.addUnit(PlayerType.PLAYER, playerUnit);
        playerUnit.receiveDamage(100);
        board.removeDeadUnits();
        assertFalse(board.getUnits(PlayerType.PLAYER).contains(playerUnit));
    }

    @Test
    void removeDeadUnits_keepsAliveCards() {
        board.addUnit(PlayerType.PLAYER, playerUnit);
        board.removeDeadUnits();
        assertTrue(board.getUnits(PlayerType.PLAYER).contains(playerUnit));
    }

    @Test
    void getEnemiesOf_returnsOppositePlayerUnits() {
        board.addUnit(PlayerType.PLAYER, playerUnit);
        board.addUnit(PlayerType.AI, aiUnit);
        assertTrue(board.getEnemiesOf(PlayerType.PLAYER).contains(aiUnit));
        assertFalse(board.getEnemiesOf(PlayerType.PLAYER).contains(playerUnit));
    }

    @Test
    void getAllUnits_returnsBothSides() {
        board.addUnit(PlayerType.PLAYER, playerUnit);
        board.addUnit(PlayerType.AI, aiUnit);
        assertTrue(board.getAllUnits().contains(playerUnit));
        assertTrue(board.getAllUnits().contains(aiUnit));
    }

    @Test
    void initiallyEmpty() {
        assertTrue(board.getUnits(PlayerType.PLAYER).isEmpty());
        assertTrue(board.getUnits(PlayerType.AI).isEmpty());
    }
}