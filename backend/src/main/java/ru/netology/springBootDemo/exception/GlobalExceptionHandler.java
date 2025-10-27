package ru.netology.springBootDemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCardDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCardDataException(InvalidCardDataException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(error); // 400
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOperationNotFoundException(OperationNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // 404
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(error); // 400
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = new ErrorResponse("Transfer failed: " + e.getMessage());
        return ResponseEntity.internalServerError().body(error); // 500
    }
}