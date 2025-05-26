package com.subscriptionservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений.
 * Перехватывает и обрабатывает все исключения, возникающие в приложении,
 * преобразуя их в соответствующие HTTP-ответы с понятными сообщениями об ошибках.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение ResourceNotFoundException.
     * Возвращает HTTP 404 (Not Found) с сообщением об ошибке.
     *
     * @param ex исключение, которое было выброшено
     * @return ResponseEntity с сообщением об ошибке и статусом 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Ресурс не найден: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение ValidationException.
     * Возвращает HTTP 400 (Bad Request) с сообщением об ошибке валидации.
     *
     * @param ex исключение, которое было выброшено
     * @return ResponseEntity с сообщением об ошибке и статусом 400
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        log.error("Ошибка валидации: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения валидации Spring (MethodArgumentNotValidException).
     * Возвращает HTTP 400 (Bad Request) со списком ошибок валидации.
     *
     * @param ex исключение, которое было выброшено
     * @return ResponseEntity с картой ошибок валидации и статусом 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.put("error", errorMessage);
        });
        log.error("Ошибки валидации: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает все непойманные исключения.
     * Возвращает HTTP 500 (Internal Server Error) с общим сообщением об ошибке.
     *
     * @param ex исключение, которое было выброшено
     * @return ResponseEntity с общим сообщением об ошибке и статусом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllUncaughtException(Exception ex) {
        log.error("Непредвиденная ошибка: ", ex);
        Map<String, String> response = new HashMap<>();
        response.put("error", "Произошла внутренняя ошибка сервера");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 