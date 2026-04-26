package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;
import com.example.projectopoopm05202300903.models.player.Player;

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
    }

    public void attackTarget(ITarget target) {
        if (hasAttackedThisTurn) return;
        target.takeAttackFrom(attack, this);
        hasAttackedThisTurn = true;
    }

    @Override
    public void takeAttackFrom(int incomingDamage, ITarget attacker) {
        receiveDamage(incomingDamage);
        attacker.receiveDamage(attack);
    }

    @Override
    public String play(Player caster, Player opponent, Board board) {
        board.addUnit(caster.getPlayerType(), this);
        return "invocou " + name + " no campo de batalha!";
    }

    @Override
    public String[][] getCardAppearance(boolean onBoard) {
        String hpText = onBoard ? currentHealth + "/" + maxHealth : String.valueOf(maxHealth);
        String[][] base = {
            {"UNIDADE",        "#e74c3c",    "#c0392b"},
            {"ATK " + attack,  "#ffd700",    "#e74c3c44",   "9px"},
            {"HP "  + hpText,  "#ff6b6b",    "#c0392b44",   "9px"}
        };
        if (!onBoard || !hasAttackedThisTurn) return base;
        return new String[][] { base[0], base[1], base[2],
            {"(usado)", "#868e96", "transparent", "7px"}
        };
    }

    @Override
    public void receiveDamage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public boolean isDead() { return currentHealth <= 0; }
    public int getAttack() { return attack; }
    public void resetAttackStatus() { hasAttackedThisTurn = false; }
    public boolean hasAttackedThisTurn() { return hasAttackedThisTurn; }
}