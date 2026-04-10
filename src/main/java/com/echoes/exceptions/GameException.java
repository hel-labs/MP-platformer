package com.echoes.exceptions;

public abstract class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
