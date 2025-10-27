package ru.netology.springBootDemo.service;

import ru.netology.springBootDemo.model.*;
import org.springframework.stereotype.Service;
import ru.netology.springBootDemo.exception.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TransferService {
    private final ConcurrentMap<String, TransferRequest> operations = new ConcurrentHashMap<>();

    public TransferResponse transfer(TransferRequest request) {
        if (request.getCardFromNumber() == null || request.getCardToNumber() == null) {
            throw new InvalidCardDataException("Missing card number");
        }

        String operationId = UUID.randomUUID().toString();
        operations.put(operationId, request);

        try {
            logToFile(
                    "TRANSFER",
                    request.getCardFromNumber(),
                    request.getCardToNumber(),
                    request.getAmount().getValue(),
                    10,
                    "OK",
                    operationId
            );
        } catch (IOException e) {
            throw new LoggingException("Failed to log transfer operation", e);
        }

        return new TransferResponse(operationId);
    }

    public TransferResponse confirm(ConfirmRequest request) {
        TransferRequest transfer = operations.get(request.getOperationId());
        if (transfer == null) {
            throw new OperationNotFoundException("Operation not found: " + request.getOperationId());
        }

        // Опционально: удаляем операцию после подтверждения
        // operations.remove(request.getOperationId());

        try {
            logToFile(
                    "CONFIRM",
                    transfer.getCardFromNumber(),
                    transfer.getCardToNumber(),
                    transfer.getAmount().getValue(),
                    0,
                    "CONFIRMED",
                    request.getOperationId()
            );
        } catch (IOException e) {
            throw new LoggingException("Failed to log confirmation operation", e);
        }

        return new TransferResponse(request.getOperationId());
    }

    private void logToFile(String type, String from, String to, int amount, int fee, String result, String opId) throws IOException {
        String log = String.format(
                "%s | %s | FROM: %s | TO: %s | AMOUNT: %d | FEE: %d | RESULT: %s | ID: %s\n",
                LocalDateTime.now(), type, from, to, amount, fee, result, opId
        );

        try (FileWriter fw = new FileWriter("transfer.log", true)) {
            fw.write(log);
        }
    }
}