package org.alphatrack.screensociety.exceptions;

public class AuthorizationFailureException extends RuntimeException {
    public AuthorizationFailureException(String message) {
        super(message);
    }
}
