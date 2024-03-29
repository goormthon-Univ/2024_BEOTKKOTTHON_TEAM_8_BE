package com.example.worrybox.utils.execption;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final Exception hiddenException;

    public EntityNotFoundException(String message) {
        super(message);
        this.hiddenException = null;
    }

    public EntityNotFoundException(String entityName, Exception exception) {
        super(entityName  + " not found. Additional info: " + exception.getMessage());
        this.hiddenException = exception;
    }

}

