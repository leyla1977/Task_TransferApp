package ru.netology.springBootDemo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.netology.springBootDemo.model.TransferRequest;
import ru.netology.springBootDemo.model.TransferResponse;
import ru.netology.springBootDemo.model.Amount;
import ru.netology.springBootDemo.model.ConfirmRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ru.netology.springBootDemo.TransferApplication.class}
)
class TransferIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void transfer_ShouldProcessSuccessfully() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setCardFromNumber("1111222233334444");
        request.setCardFromValidTill("12/25");
        request.setCardFromCVV("123");
        request.setCardToNumber("5555666677778888");

        Amount amount = new Amount();
        amount.setValue(500);
        amount.setCurrency("RUB");
        request.setAmount(amount);

        // When
        ResponseEntity<TransferResponse> response = restTemplate.postForEntity(
                "/transfer", request, TransferResponse.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getOperationId());
    }

    @Test
    void confirmOperation_ShouldConfirmSuccessfully() {
        // First create transfer
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setCardFromNumber("1111222233334444");
        transferRequest.setCardFromValidTill("12/25");
        transferRequest.setCardFromCVV("123");
        transferRequest.setCardToNumber("5555666677778888");

        Amount amount = new Amount();
        amount.setValue(500);
        amount.setCurrency("RUB");
        transferRequest.setAmount(amount);

        ResponseEntity<TransferResponse> transferResponse = restTemplate.postForEntity(
                "/transfer", transferRequest, TransferResponse.class);
        String operationId = transferResponse.getBody().getOperationId();

        // Then confirm
        ConfirmRequest confirmRequest = new ConfirmRequest();
        confirmRequest.setOperationId(operationId);
        confirmRequest.setCode("0000");

        ResponseEntity<TransferResponse> confirmResponse = restTemplate.postForEntity(
                "/confirmOperation", confirmRequest, TransferResponse.class);

        // Then
        assertEquals(HttpStatus.OK, confirmResponse.getStatusCode());
        assertEquals(operationId, confirmResponse.getBody().getOperationId());
    }
}