package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.enums.SpellType;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

    public static List<Card> createDeck() {
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            cards.add(new UnitCard("Espírito do Vento",   1, "Veloz como o ar",           1, 2));
            cards.add(new UnitCard("Salamandra de Fogo",  2, "Feroz mas frágil",           3, 1));
            cards.add(new UnitCard("Gnomo da Terra",      2, "Pequeno mas resistente",     2, 3));
            cards.add(new UnitCard("Sereia das Águas",    3, "Guarda das profundezas",     2, 4));
            cards.add(new UnitCard("Elemental do Raio",   4, "Velocidade fulminante",      5, 2));
        }

        cards.add(new UnitCard("Golem de Terra",    4, "Rocha viva e resistente",  2, 6));
        cards.add(new UnitCard("Fênix Imortal",     5, "Renasce das cinzas",       4, 4));
        cards.add(new UnitCard("Basilisco de Gelo", 5, "Gélido e poderoso",        3, 7));
        cards.add(new UnitCard("Dragão de Fogo",    6, "Terror das chamas eternas",6, 5));
        cards.add(new UnitCard("Titan do Mar",      7, "Senhor dos oceanos",       5, 9));

        for (int i = 0; i < 2; i++) {
            cards.add(new SpellCard("Toque da Vida",   1, "Restaura 2 de vida",  2, SpellType.HEAL));
            cards.add(new SpellCard("Flecha de Gelo",  2, "Causa 3 de dano",     3, SpellType.DAMAGE));
            cards.add(new SpellCard("Bola de Fogo",    3, "Causa 4 de dano",     4, SpellType.DAMAGE));
        }

        cards.add(new SpellCard("Cura das Águas",       2, "Restaura 4 de vida",  4, SpellType.HEAL));
        cards.add(new SpellCard("Explosão Elemental",   4, "Causa 5 de dano",     5, SpellType.DAMAGE));
        cards.add(new SpellCard("Tempestade de Raios",  5, "Causa 7 de dano",     7, SpellType.DAMAGE));

        return cards;
    }
}
