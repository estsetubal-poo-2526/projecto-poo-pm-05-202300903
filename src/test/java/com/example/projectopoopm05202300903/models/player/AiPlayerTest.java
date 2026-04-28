package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.enums.PlayerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiPlayerTest {

    @Test
    void getPlayerType_returnsAi() {
        AiPlayer ai = new AiPlayer("PC");
        assertEquals(PlayerType.AI, ai.getPlayerType());
    }

    @Test
    void getName_returnsConstructorValue() {
        AiPlayer ai = new AiPlayer("Oponente");
        assertEquals("Oponente", ai.getName());
    }

    @Test
    void counterAttackDamage_isZero() {
        AiPlayer ai = new AiPlayer("PC");
        assertEquals(0, ai.getCounterAttackDamage());
    }
}