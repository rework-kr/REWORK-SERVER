package com.example.rework.global.error;

public class PasswordNotMatchException extends RuntimeException  {
    public PasswordNotMatchException() {
        super();
    }
    public PasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
    public PasswordNotMatchException(String message) {
        super(message);
    }
    public PasswordNotMatchException(Throwable cause) {
        super(cause);
    }
}
