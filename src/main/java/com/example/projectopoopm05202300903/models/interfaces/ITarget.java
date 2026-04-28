package com.example.projectopoopm05202300903.models.interfaces;

public interface ITarget {
    void receiveDamage(int amount);
    void takeAttackFrom(int incomingDamage, ITarget attacker);
}