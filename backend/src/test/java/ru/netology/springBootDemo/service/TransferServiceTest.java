package ru.netology.springBootDemo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.springBootDemo.model.TransferRequest;
import ru.netology.springBootDemo.model.TransferResponse;
import ru.netology.springBootDemo.model.Amount;
import ru.netology.springBootDemo.model.ConfirmRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Test
    void transfer_ShouldReturnOperationId_WhenValidRequest() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setCardFromNumber("1234567812345678");
        request.setCardToNumber("8765432187654321");
        request.setCardFromValidTill("12/25");
        request.setCardFromCVV("123");

        Amount amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUB");
        request.setAmount(amount);

        // When
        TransferResponse response = transferService.transfer(request);

        // Then
        assertNotNull(response.getOperationId());
        assertFalse(response.getOperationId().isEmpty());
    }

    @Test
    void transfer_ShouldThrowException_WhenCardFromNumberIsNull() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setCardFromNumber(null); // null card number
        request.setCardToNumber("8765432187654321");

        Amount amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUB");
        request.setAmount(amount);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(request));
    }

    @Test
    void transfer_ShouldThrowException_WhenCardToNumberIsNull() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setCardFromNumber("1234567812345678");
        request.setCardToNumber(null); // null card number

        Amount amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUB");
        request.setAmount(amount);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(request));
    }

    @Test
    void confirm_ShouldReturnOperationId_WhenValidOperation() {
        // Given
        // Сначала создаем операцию
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setCardFromNumber("1234567812345678");
        transferRequest.setCardToNumber("8765432187654321");

        Amount amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUB");
        transferRequest.setAmount(amount);

        TransferResponse transferResponse = transferService.transfer(transferRequest);
        String operationId = transferResponse.getOperationId();

        ConfirmRequest confirmRequest = new ConfirmRequest();
        confirmRequest.setOperationId(operationId);
        confirmRequest.setCode("0000");

        // When
        TransferResponse confirmResponse = transferService.confirm(confirmRequest);

        // Then
        assertEquals(operationId, confirmResponse.getOperationId());
    }

    @Test
    void confirm_ShouldThrowException_WhenOperationNotFound() {
        // Given
        ConfirmRequest confirmRequest = new ConfirmRequest();
        confirmRequest.setOperationId("non-existent-id");
        confirmRequest.setCode("0000");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> transferService.confirm(confirmRequest));
    }
}