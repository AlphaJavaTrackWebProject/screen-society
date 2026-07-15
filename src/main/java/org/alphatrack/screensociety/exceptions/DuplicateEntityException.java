package org.alphatrack.screensociety.exceptions;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String type, String field, String value) {

        super(String.format("%s with %s %s already exists",type,field,value));

    }
}
