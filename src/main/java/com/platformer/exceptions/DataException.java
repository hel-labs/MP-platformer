package com.platformer.exceptions;

public class DataException extends GameException {
    public DataException(String message) {
        super(message);
    }
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}