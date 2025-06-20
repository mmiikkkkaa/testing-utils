package net.mikka.testing.validation;

import net.mikka.testing.ValidationError;

import java.util.List;

public interface TestObjectValidationStrategy {
    List<ValidationError> validateTestObjectCreatedByMethod(String testObjectCreatingMethodName) throws IllegalAccessException;
}
