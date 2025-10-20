package ru.netology.springBootDemo.controller;

import ru.netology.springBootDemo.model.*;
import ru.netology.springBootDemo.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@RestController
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            TransferResponse response = service.transfer(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "Invalid input: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(problem);
        } catch (Exception e) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Transfer failed: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(problem);
        }
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<?> confirm(@RequestBody ConfirmRequest request) {
        try {
            TransferResponse response = service.confirm(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "Invalid code or operationId"
            );
            return ResponseEntity.badRequest().body(problem);
        } catch (Exception e) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Confirmation failed: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(problem);
        }
    }
}
