package com.example.rework.global.error;

public class EncryptException extends RuntimeException {
    public EncryptException() {
        super();
    }
    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
    public EncryptException(String message) {
        super(message);
    }
    public EncryptException(Throwable cause) {
        super(cause);
    }
}