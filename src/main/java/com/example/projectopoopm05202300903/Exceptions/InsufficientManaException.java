package com.example.projectopoopm05202300903.Exceptions;

public class InsufficientManaException extends Exception {

    public InsufficientManaException() {
        super("Mana insuficiente para jogar esta carta!");
    }

    public InsufficientManaException(String message) {
        super(message);
    }
}
