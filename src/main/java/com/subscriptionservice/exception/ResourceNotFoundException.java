package com.subscriptionservice.exception;

/**
 * Исключение, возникающее при попытке доступа к несуществующему ресурсу.
 * Используется для обработки ситуаций, когда запрашиваемый ресурс не найден в системе.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
} 