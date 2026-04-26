package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private HumanPlayer player;

    @BeforeEach
    void setUp() {
        player = new HumanPlayer("Jogador");
        for (int i = 0; i < 10; i++)
            player.getDeck().addCard(new UnitCard("C" + i, 1, "", 1, 1));
    }

    @Test
    void startTurn_drawsCardIntoHand() throws EmptyDeckException {
        int handBefore = player.getHand().getCards().size();
        player.startTurn();
        assertEquals(handBefore + 1, player.getHand().getCards().size());
    }

    @Test
    void startTurn_increasesManaByOne() throws EmptyDeckException {
        player.startTurn();
        assertEquals(1, player.getCurrentMana());
    }

    @Test
    void startTurn_manaCapAt10() throws EmptyDeckException {
        for (int i = 0; i < 15; i++) player.startTurn();
        assertEquals(10, player.getMaxMana());
        assertEquals(10, player.getCurrentMana());
    }

    @Test
    void startTurn_onEmptyDeck_throwsException() {
        HumanPlayer empty = new HumanPlayer("Empty");
        assertThrows(EmptyDeckException.class, empty::startTurn);
    }

    @Test
    void playCard_spendsMana() throws EmptyDeckException, InsufficientManaException {
        player.startTurn(); // 1 mana
        UnitCard card = new UnitCard("X", 1, "", 1, 1);
        player.getHand().addCard(card);
        player.playCard(card);
        assertEquals(0, player.getCurrentMana());
    }

    @Test
    void playCard_removesCardFromHand() throws EmptyDeckException, InsufficientManaException {
        player.startTurn();
        UnitCard card = new UnitCard("X", 1, "", 1, 1);
        player.getHand().addCard(card);
        player.playCard(card);
        assertFalse(player.getHand().getCards().contains(card));
    }

    @Test
    void playCard_withInsufficientMana_throwsException() throws EmptyDeckException {
        player.startTurn(); // 1 mana
        UnitCard expensive = new UnitCard("Expensive", 5, "", 1, 1);
        player.getHand().addCard(expensive);
        assertThrows(InsufficientManaException.class, () -> player.playCard(expensive));
    }

    @Test
    void receiveDamage_reducesHp() {
        player.receiveDamage(5);
        assertEquals(15, player.getCurrentHp());
    }

    @Test
    void receiveDamage_cannotGoBelowZero() {
        player.receiveDamage(100);
        assertEquals(0, player.getCurrentHp());
    }

    @Test
    void receiveHealing_increasesHp() {
        player.receiveDamage(10);
        player.receiveHealing(5);
        assertEquals(15, player.getCurrentHp());
    }

    @Test
    void receiveHealing_cannotExceedMaxHp() {
        player.receiveHealing(100);
        assertEquals(player.getMaxHp(), player.getCurrentHp());
    }

    @Test
    void isDead_whenHpIsZero() {
        player.receiveDamage(player.getMaxHp());
        assertTrue(player.isDead());
    }

    @Test
    void isDead_false_initially() {
        assertFalse(player.isDead());
    }

    @Test
    void getCounterAttackDamage_returnsZero() {
        assertEquals(0, player.getCounterAttackDamage());
    }

    @Test
    void getPlayerType_humanPlayer_returnsPlayer() {
        assertEquals(com.example.projectopoopm05202300903.models.enums.PlayerType.PLAYER,
                player.getPlayerType());
    }

    @Test
    void getPlayerType_aiPlayer_returnsAi() {
        AiPlayer ai = new AiPlayer("PC");
        assertEquals(com.example.projectopoopm05202300903.models.enums.PlayerType.AI,
                ai.getPlayerType());
    }
}