package ru.netology.springBootDemo.model;

public class Amount {
    private int value;
    private String currency;

    // Геттеры
    public int getValue() { return value; }
    public String getCurrency() { return currency; }

    // Сеттеры
    public void setValue(int value) { this.value = value; }
    public void setCurrency(String currency) { this.currency = currency; }
}