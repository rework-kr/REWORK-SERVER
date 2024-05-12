package com.example.rework.global.error;

public class DuplicateAccountException extends RuntimeException {
    public DuplicateAccountException() {
        super();
    }
    public DuplicateAccountException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateAccountException(String message) {
        super(message);
    }
    public DuplicateAccountException(Throwable cause) {
        super(cause);
    }

}