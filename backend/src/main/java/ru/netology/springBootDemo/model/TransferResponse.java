package ru.netology.springBootDemo.model;

public class TransferResponse {
    private String operationId;

    public TransferResponse() {}

    public TransferResponse(String operationId) {
        this.operationId = operationId;
    }

    // Геттер
    public String getOperationId() { return operationId; }

    // Сеттер
    public void setOperationId(String operationId) { this.operationId = operationId; }
}