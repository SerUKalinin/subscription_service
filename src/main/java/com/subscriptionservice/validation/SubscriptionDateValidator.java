package com.subscriptionservice.validation;

import com.subscriptionservice.dto.SubscriptionDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Валидатор для проверки дат подписки.
 * Проверяет, что дата окончания подписки не раньше даты начала.
 */
public class SubscriptionDateValidator implements ConstraintValidator<SubscriptionDateConstraint, SubscriptionDTO> {

    @Override
    public void initialize(SubscriptionDateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(SubscriptionDTO subscription, ConstraintValidatorContext context) {
        if (subscription == null || subscription.getStartDate() == null || subscription.getEndDate() == null) {
            return true;
        }
        return subscription.getEndDate().isAfter(subscription.getStartDate());
    }
} 