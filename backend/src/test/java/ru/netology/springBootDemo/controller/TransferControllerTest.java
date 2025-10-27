package ru.netology.springBootDemo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.springBootDemo.exception.InvalidCardDataException;
import ru.netology.springBootDemo.exception.OperationNotFoundException;
import ru.netology.springBootDemo.model.*;
import ru.netology.springBootDemo.service.TransferService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransferControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Используйте реальный GlobalExceptionHandler
        this.mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .setControllerAdvice(new ru.netology.springBootDemo.exception.GlobalExceptionHandler())
                .build();
    }

    // Встроенный обработчик исключений для тестов
    @RestControllerAdvice
    public static class TestExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(InvalidCardDataException e) {
            ErrorResponse error = new ErrorResponse("BAD_REQUEST", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
            ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Простой класс для ошибок
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }

    @Test
    public void testTransfer_Success() throws Exception {
        // Given
        TransferResponse response = new TransferResponse("test-operation-id");

        when(transferService.transfer(any(TransferRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "cardFromNumber": "1234567812345678",
                        "cardFromValidTill": "12/25",
                        "cardFromCVV": "123",
                        "cardToNumber": "8765432187654321",
                        "amount": {
                            "value": 1000,
                            "currency": "RUB"
                        }
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value("test-operation-id"));
    }

    @Test
    public void testTransfer_InvalidData() throws Exception {
        // Given
        when(transferService.transfer(any(TransferRequest.class)))
                .thenThrow(new InvalidCardDataException("Missing card number"));

        // When & Then
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing card number")); // только message
    }

    @Test
    public void testConfirmOperation_Success() throws Exception {
        // Given
        TransferResponse response = new TransferResponse("test-operation-id");

        when(transferService.confirm(any(ConfirmRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "operationId": "test-operation-id",
                        "code": "0000"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value("test-operation-id"));
    }

    @Test
    public void testConfirmOperation_OperationNotFound() throws Exception {
        // Given
        when(transferService.confirm(any(ConfirmRequest.class)))
                .thenThrow(new OperationNotFoundException("Operation not found"));

        // When & Then
        mockMvc.perform(post("/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "operationId": "non-existent-id",
                    "code": "0000"
                }
                """))
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.message").value("Operation not found")); // только message
    }
}