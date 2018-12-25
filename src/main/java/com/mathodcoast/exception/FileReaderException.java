package com.mathodcoast.exception;

public class FileReaderException extends RuntimeException {
    public FileReaderException(String message,Exception e) {
        super(message, e);
    }
}
