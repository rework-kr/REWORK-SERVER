package com.example.rework.global.error;

public class AlreadyPagingIdException extends RuntimeException {
    public AlreadyPagingIdException() {
        super();
    }
    public AlreadyPagingIdException(String message, Throwable cause) {
        super(message, cause);
    }
    public AlreadyPagingIdException(String message) {
        super(message);
    }
    public AlreadyPagingIdException(Throwable cause) {
        super(cause);
    }

}