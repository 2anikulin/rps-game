package com.anikulin.rps.core;

/**
 * Exception.
 * Incorrect type of RPSType enum.
 */
public class RPSTypeException extends Exception {

    /**
     * Constructor.
     * @param message Error message.
     */
    public RPSTypeException(String message) {
        super(message);
    }
}
