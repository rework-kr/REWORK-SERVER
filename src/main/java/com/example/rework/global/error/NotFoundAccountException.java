package com.example.rework.global.error;

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException() {
        super();
    }
    public NotFoundAccountException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundAccountException(String message) {
        super(message);
    }
    public NotFoundAccountException(Throwable cause) {
        super(cause);
    }
}