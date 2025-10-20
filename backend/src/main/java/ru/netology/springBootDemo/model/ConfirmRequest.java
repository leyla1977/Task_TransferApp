package ru.netology.springBootDemo.model;


public class ConfirmRequest {
    private String operationId;
    private String code;

    // Геттеры
    public String getOperationId() { return operationId; }
    public String getCode() { return code; }

    // Сеттеры
    public void setOperationId(String operationId) { this.operationId = operationId; }
    public void setCode(String code) { this.code = code; }
}