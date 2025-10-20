package ru.netology.springBootDemo.model;

public class ErrorResponse {
    public String message;
    public int id;

    public ErrorResponse(String message, int id) {
        this.message = message;
        this.id = id;
    }
}