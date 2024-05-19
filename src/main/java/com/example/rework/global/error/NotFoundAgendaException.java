package com.example.rework.global.error;

import org.springframework.web.client.ResourceAccessException;

public class NotFoundAgendaException extends RuntimeException {
    public NotFoundAgendaException() {
        super();
    }

    public NotFoundAgendaException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAgendaException(String message) {
        super(message);
    }

    public NotFoundAgendaException(Throwable cause) {
        super(cause);
    }

}
