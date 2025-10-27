package ru.netology.springBootDemo.exception;

public class InvalidCardDataException extends RuntimeException {
    public InvalidCardDataException(String message) {
        super(message);
    }
}
