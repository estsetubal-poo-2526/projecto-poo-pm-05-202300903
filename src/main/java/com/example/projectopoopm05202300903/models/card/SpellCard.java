package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.enums.SpellType;
import com.example.projectopoopm05202300903.models.player.Player;

public class SpellCard extends Card {
    private final int effectValue;
    private final SpellType type;

    public SpellCard(String name, int manaCost, String description, int effectValue, SpellType type) {
        super(name, manaCost, description);
        this.effectValue = effectValue;
        this.type = type;
    }

    @Override
    public String play(Player caster, Player opponent, Board board) {
        if (type == SpellType.HEAL) {
            caster.receiveHealing(effectValue);
            return "usou " + name + " e recuperou " + effectValue + " de vida!";
        }
        opponent.receiveDamage(effectValue);
        return "usou " + name + " e causou " + effectValue + " de dano a " + opponent.getName() + "!";
    }

    @Override
    public String[][] getCardAppearance(boolean onBoard) {
        boolean isDmg = type == SpellType.DAMAGE;
        return new String[][] {
            {"MAGIA", "#8e44ad", "#8e44ad"},
            {(isDmg ? "DMG " : "CURA ") + effectValue,
             isDmg ? "#ff6b6b" : "#69db7c",
             "#00000055",
             "9px"}
        };
    }

    public int getEffectValue() { return this.effectValue; }
    public SpellType getType() { return this.type; }
}