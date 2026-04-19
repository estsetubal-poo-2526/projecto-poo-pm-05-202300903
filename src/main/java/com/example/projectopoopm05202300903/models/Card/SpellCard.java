package com.example.projectopoopm05202300903.models.Card;

import com.example.projectopoopm05202300903.models.Enums.SpellType;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;

public class SpellCard extends Card {
    private int effectValue;
    private SpellType type;

    public SpellCard(String name, int manaCost, String description, int effectValue, SpellType type) {
        super(name, manaCost, description);
        this.effectValue = effectValue;
        this.type = type;
    }

    @Override
    public void applyEffect(ITarget target) {
        // TODO: Implement this after the game engine being done
    }

    public int getEffectValue() {
        return effectValue;
    }

    public SpellType getType() {
        return type;
    }

    public void setEffectValue(int effectValue) {
        this.effectValue = effectValue;
    }

    public void setType(SpellType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() + " spell: [" + type + ": " + effectValue + " | Mana: " + getManaCost() + "]";
    }
}
