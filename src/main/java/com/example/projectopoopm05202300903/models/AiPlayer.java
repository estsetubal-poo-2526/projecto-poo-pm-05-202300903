package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.Enums.SpellType;
import com.example.projectopoopm05202300903.Exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.interfaces.ITarget;

import java.util.List;

public class AiPlayer extends Player {

    private Board board;

    public AiPlayer(String name, int maxHp, Deck deck) {
        super(name, maxHp, deck);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void executeTurn() {
        startTurnProcess();
        playCardsFromHand();
        attackWithUnits();
    }

    private void playCardsFromHand() {
        List<Card> playableCards = getHand().getPlayableCards(getCurrentMana());

        while (!playableCards.isEmpty()) {
            Card cardToPlay = playableCards.stream()
                    .max((c1, c2) -> Integer.compare(c1.getManaCost(), c2.getManaCost()))
                    .orElse(null);

            try {
                ITarget target = chooseTarget(cardToPlay);
                if (target != null) {
                    playCard(cardToPlay, target);

                    if (cardToPlay instanceof UnitCard && board != null) {
                        board.addUnit(this, (UnitCard) cardToPlay);
                    }
                }
            } catch (InsufficientManaException e) {
                break;
            }

            playableCards = getHand().getPlayableCards(getCurrentMana());
        }
    }

    private ITarget chooseTarget(Card card) {
        if (card instanceof UnitCard) {
            return this;
        }

        if (card instanceof SpellCard) {
            SpellCard spell = (SpellCard) card;

            if (spell.getType() == SpellType.DAMAGE) {
                if (board != null) {
                    return null;
                }
            }

            if (spell.getType() == SpellType.HEAL) {
                if (getCurrentHp() < getMaxHp()) {
                    return this;
                }
            }
        }

        return null;
    }

    private void attackWithUnits() {
        if (board == null) return;

        List<UnitCard> myUnits = board.getUnits(this);
        for (UnitCard unit : myUnits) {
            if (!unit.hasAttackedThisTurn() && unit.isAlive()) {
                // A lógica de escolha de alvo será processada pelo GameEngine
                // Aqui apenas marcamos que a unidade está pronta para atacar
            }
        }
    }
}
