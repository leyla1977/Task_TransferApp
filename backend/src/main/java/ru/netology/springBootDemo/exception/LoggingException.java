package ru.netology.springBootDemo.exception;

public class LoggingException extends RuntimeException {
    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggingException(String message) {
        super(message);
    }
}