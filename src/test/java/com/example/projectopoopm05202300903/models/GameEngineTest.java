package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.SpellCard;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.PlayerType;
import com.example.projectopoopm05202300903.models.enums.SpellType;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() throws EmptyDeckException {
        engine = new GameEngine("Jogador", _ -> {});
        engine.startGame();
    }

    // ── Initialisation ────────────────────────────────────────────────────────

    @Test
    void startGame_humanHasFourCardsInHand() {
        // 3 dealt + 1 drawn by startTurn
        assertEquals(4, engine.getHumanPlayer().getHand().getCards().size());
    }

    @Test
    void startGame_aiHasThreeCardsInHand() {
        assertEquals(3, engine.getAiPlayer().getHand().getCards().size());
    }

    @Test
    void startGame_humanHasOneMana() {
        assertEquals(1, engine.getHumanPlayer().getCurrentMana());
    }

    @Test
    void startGame_gameNotOver() {
        assertFalse(engine.isGameOver());
        assertNull(engine.getWinnerName());
    }

    @Test
    void startGame_isHumanTurn() {
        assertTrue(engine.isHumanTurn());
    }

    // ── Playing cards ─────────────────────────────────────────────────────────

    @Test
    void playUnitCard_addsUnitToPlayerBoard() throws InsufficientManaException {
        UnitCard unit = new UnitCard("Teste", 1, "", 2, 3);
        engine.getHumanPlayer().getHand().addCard(unit);
        engine.playCardFromHand(unit);
        assertTrue(engine.getBoard().getUnits(PlayerType.PLAYER).contains(unit));
    }

    @Test
    void playDamageSpell_reducesAiHp() throws InsufficientManaException {
        int hpBefore = engine.getAiPlayer().getCurrentHp();
        SpellCard dmg = new SpellCard("Raio", 1, "", 3, SpellType.DAMAGE);
        engine.getHumanPlayer().getHand().addCard(dmg);
        engine.playCardFromHand(dmg);
        assertEquals(hpBefore - 3, engine.getAiPlayer().getCurrentHp());
    }

    @Test
    void playHealSpell_increasesPlayerHp() throws InsufficientManaException {
        engine.getHumanPlayer().receiveDamage(5);
        int hpBefore = engine.getHumanPlayer().getCurrentHp();
        SpellCard heal = new SpellCard("Cura", 1, "", 3, SpellType.HEAL);
        engine.getHumanPlayer().getHand().addCard(heal);
        engine.playCardFromHand(heal);
        assertEquals(hpBefore + 3, engine.getHumanPlayer().getCurrentHp());
    }

    @Test
    void playCard_withInsufficientMana_throwsException() {
        UnitCard expensive = new UnitCard("Caro", 10, "", 5, 5);
        engine.getHumanPlayer().getHand().addCard(expensive);
        assertThrows(InsufficientManaException.class,
                () -> engine.playCardFromHand(expensive));
    }

    // ── Combat ────────────────────────────────────────────────────────────────

    @Test
    void processAttack_unitVsUnit_dealsMutualDamage() {
        UnitCard attacker = new UnitCard("Att", 0, "", 3, 5);
        UnitCard defender = new UnitCard("Def", 0, "", 2, 4);
        engine.getBoard().addUnit(PlayerType.PLAYER, attacker);
        engine.getBoard().addUnit(PlayerType.AI,     defender);

        engine.processAttack(attacker, defender);

        assertEquals(1, defender.getCurrentHealth()); // 4 - 3
        assertEquals(3, attacker.getCurrentHealth()); // 5 - 2
    }

    @Test
    void processAttack_unitVsPlayer_dealsDamageToPlayer() {
        UnitCard attacker = new UnitCard("Att", 0, "", 5, 5);
        engine.getBoard().addUnit(PlayerType.PLAYER, attacker);
        int hpBefore = engine.getAiPlayer().getCurrentHp();

        engine.processAttack(attacker, engine.getAiPlayer());

        assertEquals(hpBefore - 5, engine.getAiPlayer().getCurrentHp());
    }

    @Test
    void processAttack_playerTakesNoCounterDamageFromPlayer() {
        UnitCard attacker = new UnitCard("Att", 0, "", 5, 5);
        engine.getBoard().addUnit(PlayerType.PLAYER, attacker);

        engine.processAttack(attacker, engine.getAiPlayer());

        // Attacker should not take damage from player (counter = 0)
        assertEquals(5, attacker.getCurrentHealth());
    }

    @Test
    void processAttack_deadUnitRemovedFromBoard() {
        UnitCard attacker = new UnitCard("Att", 0, "", 10, 5);
        UnitCard weakDef  = new UnitCard("Def", 0, "", 0,  1);
        engine.getBoard().addUnit(PlayerType.PLAYER, attacker);
        engine.getBoard().addUnit(PlayerType.AI,     weakDef);

        engine.processAttack(attacker, weakDef);

        assertFalse(engine.getBoard().getUnits(PlayerType.AI).contains(weakDef));
    }

    // ── Win conditions ────────────────────────────────────────────────────────

    @Test
    void processAttack_aiDies_setsGameOver() {
        UnitCard finisher = new UnitCard("Finisher", 0, "", 20, 1);
        engine.getBoard().addUnit(PlayerType.PLAYER, finisher);

        engine.processAttack(finisher, engine.getAiPlayer());

        assertTrue(engine.isGameOver());
        assertEquals("Jogador", engine.getWinnerName());
    }

    @Test
    void processAttack_playerDies_setsGameOver() {
        UnitCard finisher = new UnitCard("Finisher", 0, "", 20, 1);
        engine.getBoard().addUnit(PlayerType.AI, finisher);

        engine.processAttack(finisher, engine.getHumanPlayer());

        assertTrue(engine.isGameOver());
        assertEquals("AI - Oponente", engine.getWinnerName());
    }

    @Test
    void endHumanTurn_switchesToNextTurn() {
        engine.endHumanTurn();
        assertTrue(engine.isHumanTurn()); // back to human after AI turn
    }

    @Test
    void isGameOver_false_initially() {
        assertFalse(engine.isGameOver());
    }
}