package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.enums.SpellType;
import com.example.projectopoopm05202300903.models.player.HumanPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpellCardTest {

    private HumanPlayer caster;
    private HumanPlayer opponent;
    private Board board;

    @BeforeEach
    void setUp() {
        caster   = new HumanPlayer("Caster");
        opponent = new HumanPlayer("Opponent");
        board    = new Board();
    }

    @Test
    void healSpell_increasesHpOfCaster() {
        caster.receiveDamage(5);
        int hpBefore = caster.getCurrentHp();
        SpellCard heal = new SpellCard("Cura", 1, "", 3, SpellType.HEAL);
        heal.play(caster, opponent, board);
        assertEquals(hpBefore + 3, caster.getCurrentHp());
    }

    @Test
    void healSpell_cannotExceedMaxHp() {
        SpellCard heal = new SpellCard("Cura", 1, "", 50, SpellType.HEAL);
        heal.play(caster, opponent, board);
        assertEquals(caster.getMaxHp(), caster.getCurrentHp());
    }

    @Test
    void damageSpell_reducesOpponentHp() {
        int hpBefore = opponent.getCurrentHp();
        SpellCard dmg = new SpellCard("Bola de Fogo", 3, "", 4, SpellType.DAMAGE);
        dmg.play(caster, opponent, board);
        assertEquals(hpBefore - 4, opponent.getCurrentHp());
    }

    @Test
    void damageSpell_doesNotAffectCaster() {
        int casterHp = caster.getCurrentHp();
        SpellCard dmg = new SpellCard("Raio", 2, "", 3, SpellType.DAMAGE);
        dmg.play(caster, opponent, board);
        assertEquals(casterHp, caster.getCurrentHp());
    }

    @Test
    void play_returnsHealMessage() {
        SpellCard heal = new SpellCard("Toque", 1, "", 2, SpellType.HEAL);
        caster.receiveDamage(5);
        String msg = heal.play(caster, opponent, board);
        assertTrue(msg.contains("recuperou") && msg.contains("2"));
    }

    @Test
    void play_returnsDamageMessage() {
        SpellCard dmg = new SpellCard("Flecha", 2, "", 3, SpellType.DAMAGE);
        String msg = dmg.play(caster, opponent, board);
        assertTrue(msg.contains("sofreu") && msg.contains("3"));
    }

    @Test
    void getters_returnConstructorValues() {
        SpellCard spell = new SpellCard("Explosão", 4, "Desc", 5, SpellType.DAMAGE);
        assertEquals("Explosão", spell.getName());
        assertEquals(4, spell.getManaCost());
        assertEquals(5, spell.getEffectValue());
        assertEquals(SpellType.DAMAGE, spell.getType());
    }
}