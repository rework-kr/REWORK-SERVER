package com.example.rework.global.error;

public class InvalidDiscordMessage extends RuntimeException {
    public InvalidDiscordMessage() {
        super();
    }
    public InvalidDiscordMessage(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidDiscordMessage(String message) {
        super(message);
    }
    public InvalidDiscordMessage(Throwable cause) {
        super(cause);
    }

}