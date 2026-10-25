package com.dylej.taskapi.exception;

public class DuplicateTaskException 
extends RuntimeException {
    public DuplicateTaskException(String message) {
        super(message);
    }
}
