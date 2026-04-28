package com.example.projectopoopm05202300903.models.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitCardTest {

    private UnitCard attacker;
    private UnitCard target;

    @BeforeEach
    void setUp() {
        attacker = new UnitCard("Atacante", 2, "", 3, 5);
        target   = new UnitCard("Alvo",    2, "", 2, 4);
    }

    @Test
    void attack_dealsCorrectDamageToTarget() {
        attacker.attackTarget(target);
        assertEquals(1, target.getCurrentHealth()); // 4 - 3 = 1
    }

    @Test
    void attack_triggersCounterAttack() {
        attacker.attackTarget(target);
        assertEquals(3, attacker.getCurrentHealth()); // 5 - 2 = 3
    }

    @Test
    void attack_setsHasAttackedFlag() {
        attacker.attackTarget(target);
        assertTrue(attacker.hasAttackedThisTurn());
    }

    @Test
    void attack_isBlockedWhenAlreadyAttackedThisTurn() {
        UnitCard noAttack = new UnitCard("Weak", 0, "", 0, 10);
        attacker.attackTarget(noAttack);
        int healthAfterFirst = noAttack.getCurrentHealth();

        attacker.attackTarget(noAttack); // second attack — blocked
        assertEquals(healthAfterFirst, noAttack.getCurrentHealth());
    }

    @Test
    void resetAttackStatus_allowsAttackingAgain() {
        UnitCard dummy = new UnitCard("Dummy", 0, "", 0, 100);
        attacker.attackTarget(dummy);
        attacker.resetAttackStatus();
        assertFalse(attacker.hasAttackedThisTurn());
        attacker.attackTarget(dummy);
        assertEquals(94, dummy.getCurrentHealth()); // 100 - 3 - 3
    }

    @Test
    void receiveDamage_cannotGoBelowZero() {
        attacker.receiveDamage(100);
        assertEquals(0, attacker.getCurrentHealth());
    }

    @Test
    void receiveHealing_cannotExceedMaxHealth() {
        attacker.receiveDamage(3);
        attacker.receiveHealing(100);
        assertEquals(attacker.getMaxHealth(), attacker.getCurrentHealth());
    }

    @Test
    void isDead_whenHealthIsZero() {
        attacker.receiveDamage(attacker.getMaxHealth());
        assertTrue(attacker.isDead());
    }

    @Test
    void isNotDead_whenHealthAboveZero() {
        assertFalse(attacker.isDead());
    }

    @Test
    void getCounterAttackDamage_returnsAttackStat() {
        assertEquals(3, attacker.getCounterAttackDamage());
    }

    @Test
    void getters_returnConstructorValues() {
        assertEquals("Atacante", attacker.getName());
        assertEquals(2, attacker.getManaCost());
        assertEquals(3, attacker.getAttack());
        assertEquals(5, attacker.getMaxHealth());
        assertEquals(5, attacker.getCurrentHealth());
    }
}