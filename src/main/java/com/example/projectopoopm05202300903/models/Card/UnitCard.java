package com.example.projectopoopm05202300903.models.Card;

import com.example.projectopoopm05202300903.models.interfaces.ITarget;

public class UnitCard extends Card implements ITarget {
    private int attack;
    private int maxHealth;
    private int currentHealth;
    private boolean hasAttackedThisTurn;


    public UnitCard(String name, int manaCost, String description, int attack, int maxHealth) {
        super(name, manaCost, description);
        this.attack = attack;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.hasAttackedThisTurn = false;
    }

    @Override
    public void applyEffect(ITarget target) {
        // TODO: Implement this after the game engine being done
    }

    public void attackTarget(ITarget target) {
        if (!hasAttackedThisTurn && isAlive()) {
            target.receiveDamage(this.attack);
            this.hasAttackedThisTurn = true;
        }
    }

    @Override
    public void receiveDamage(int amount) {
        if (amount > 0) {
            this.currentHealth -= amount;
        }
    }

    @Override
    public void receiveHealing(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.min(this.currentHealth + amount, this.maxHealth);
        }
    }

    @Override
    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    public void resetAttackStatus() {
        this.hasAttackedThisTurn = false;
    }

    public boolean hasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public int getAttack() {
        return attack;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    @Override
    public String toString() {
        return super.toString() + " unit: [ATK: " + attack + " | HP: " + currentHealth + "/" + maxHealth + " | Mana: " + getManaCost() + "]";
    }
}