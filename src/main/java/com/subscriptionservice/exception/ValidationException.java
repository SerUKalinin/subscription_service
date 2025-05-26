package com.subscriptionservice.exception;

/**
 * Исключение, возникающее при нарушении правил валидации данных.
 * Используется для обработки ситуаций, когда входные данные не соответствуют бизнес-правилам.
 */
public class ValidationException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке валидации
     */
    public ValidationException(String message) {
        super(message);
    }
} 