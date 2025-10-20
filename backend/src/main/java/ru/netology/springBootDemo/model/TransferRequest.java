package ru.netology.springBootDemo.model;

public class TransferRequest {
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;

    // Геттеры
    public String getCardFromNumber() { return cardFromNumber; }
    public String getCardFromValidTill() { return cardFromValidTill; }
    public String getCardFromCVV() { return cardFromCVV; }
    public String getCardToNumber() { return cardToNumber; }
    public Amount getAmount() { return amount; }

    // Сеттеры
    public void setCardFromNumber(String cardFromNumber) { this.cardFromNumber = cardFromNumber; }
    public void setCardFromValidTill(String cardFromValidTill) { this.cardFromValidTill = cardFromValidTill; }
    public void setCardFromCVV(String cardFromCVV) { this.cardFromCVV = cardFromCVV; }
    public void setCardToNumber(String cardToNumber) { this.cardToNumber = cardToNumber; }
    public void setAmount(Amount amount) { this.amount = amount; }
}


