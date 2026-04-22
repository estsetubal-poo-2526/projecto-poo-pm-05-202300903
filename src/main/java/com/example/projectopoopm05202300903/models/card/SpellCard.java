package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.enums.SpellType;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;

public class SpellCard extends Card {
    private final int effectValue;
    private final SpellType type;

    public SpellCard(String name, int manaCost, String description, int effectValue, SpellType type) {
        super(name, manaCost, description);
        this.effectValue = effectValue;
        this.type = type;
    }

    @Override
    public void applyEffect(ITarget target) {
        switch (type) {
            case DAMAGE -> target.receiveDamage(effectValue);
            case HEAL   -> target.receiveHealing(effectValue);
        }
    }

    public int getEffectValue() { return effectValue; }
    public SpellType getType() { return type; }
}