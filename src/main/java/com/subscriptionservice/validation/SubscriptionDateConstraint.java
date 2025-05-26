package com.subscriptionservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации дат подписки.
 * Проверяет, что дата окончания подписки не раньше даты начала.
 */
@Documented
@Constraint(validatedBy = SubscriptionDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubscriptionDateConstraint {
    String message() default "Дата окончания подписки должна быть позже даты начала";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 