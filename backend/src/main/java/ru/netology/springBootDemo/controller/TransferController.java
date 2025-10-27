package ru.netology.springBootDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.springBootDemo.model.TransferRequest;
import ru.netology.springBootDemo.model.ConfirmRequest;
import ru.netology.springBootDemo.model.TransferResponse;
import ru.netology.springBootDemo.service.TransferService;

@RestController
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        TransferResponse response = service.transfer(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<TransferResponse> confirm(@RequestBody ConfirmRequest request) {
        TransferResponse response = service.confirm(request);
        return ResponseEntity.ok(response);
    }
}