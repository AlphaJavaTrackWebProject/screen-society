package org.alphatrack.screensociety.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String type, String field, String value) {
        super(String.format("%s with %s %s doesnt exists", type, field, value));
    }
}
