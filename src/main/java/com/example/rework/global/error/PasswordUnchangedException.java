package com.example.rework.global.error;

public class PasswordUnchangedException extends RuntimeException {
    public PasswordUnchangedException() {
        super();
    }
    public PasswordUnchangedException(String message, Throwable cause) {
        super(message, cause);
    }
    public PasswordUnchangedException(String message) {
        super(message);
    }
    public PasswordUnchangedException(Throwable cause) {
        super(cause);
    }

}