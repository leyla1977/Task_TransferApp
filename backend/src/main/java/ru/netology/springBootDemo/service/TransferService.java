package ru.netology.springBootDemo.service;

import ru.netology.springBootDemo.model.*;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;


@Service
public class TransferService {
    private final Map<String, TransferRequest> operations = new HashMap<>();

    public TransferResponse transfer(TransferRequest request) {
        if (request.getCardFromNumber() == null || request.getCardToNumber() == null) {
            throw new IllegalArgumentException("Missing card number");
        }

        String operationId = UUID.randomUUID().toString();
        operations.put(operationId, request);

        logToFile(
                "TRANSFER",
                request.getCardFromNumber(),
                request.getCardToNumber(),
                request.getAmount().getValue(),
                10,
                "OK",
                operationId
        );

        return new TransferResponse(operationId);
    }

    public TransferResponse confirm(ConfirmRequest request) {
        if (!operations.containsKey(request.getOperationId())) {
            throw new IllegalArgumentException("Operation not found");
        }

        TransferRequest transfer = operations.get(request.getOperationId());

        logToFile(
                "CONFIRM",
                transfer.getCardFromNumber(),
                transfer.getCardToNumber(),
                transfer.getAmount().getValue(),
                0,
                "CONFIRMED",
                request.getOperationId()
        );

        return new TransferResponse(request.getOperationId());
    }

    private void logToFile(String type, String from, String to, int amount, int fee, String result, String opId) {
        String log = String.format(
                "%s | %s | FROM: %s | TO: %s | AMOUNT: %d | FEE: %d | RESULT: %s | ID: %s\n",
                LocalDateTime.now(), type, from, to, amount, fee, result, opId
        );

        try (FileWriter fw = new FileWriter("transfer.log", true)) {
            fw.write(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

