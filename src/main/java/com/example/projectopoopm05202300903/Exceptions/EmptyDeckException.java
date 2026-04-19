package com.example.projectopoopm05202300903.Exceptions;

public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
        super("O baralho está vazio! Derrota imediata.");
    }

    public EmptyDeckException(String message) {
        super(message);
    }
}
