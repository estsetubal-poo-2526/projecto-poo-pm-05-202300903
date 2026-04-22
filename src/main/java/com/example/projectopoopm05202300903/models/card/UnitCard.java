package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.interfaces.ITarget;

public class UnitCard extends Card implements ITarget {
    private final int attack;
    private final int maxHealth;
    private int currentHealth;
    private boolean hasAttackedThisTurn;

    public UnitCard(String name, int manaCost, String description, int attack, int health) {
        super(name, manaCost, description);
        this.attack = attack;
        this.maxHealth = health;
        this.currentHealth = health;
        this.hasAttackedThisTurn = false;
    }

    public void attackTarget(ITarget target) {
        if (hasAttackedThisTurn) return;
        target.receiveDamage(attack);
        if (target instanceof UnitCard enemy) {
            receiveDamage(enemy.getAttack());
        }
        hasAttackedThisTurn = true;
    }

    @Override
    public void receiveDamage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    @Override
    public void receiveHealing(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    @Override
    public boolean isDead() {
        return currentHealth <= 0;
    }

    @Override
    public void applyEffect(ITarget target) {
        attackTarget(target);
    }

    public void resetAttackStatus() {
        hasAttackedThisTurn = false;
    }

    public int getAttack() { return attack; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public boolean hasAttackedThisTurn() { return hasAttackedThisTurn; }
}