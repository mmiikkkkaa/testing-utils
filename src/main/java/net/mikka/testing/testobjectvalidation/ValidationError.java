package net.mikka.testing.testobjectvalidation;

import lombok.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Value
public class ValidationError {
    String className;
    String fieldName;
    String fieldType;
    ValidationErrorType errorType;
    String object;
    Class<? extends Annotation> annotation;

    public static ValidationError of(Class<?> clazz, Field field, ValidationErrorType errorType, Object object, Class<? extends Annotation> annotation) {
        return new ValidationError(
                clazz.getName(),
                field.getName(),
                field.getType().getName(),
                errorType,
                object.toString(),
                annotation
        );
    }

    public static ValidationError of(Class<?> clazz, Field field, ValidationErrorType errorType, Object object) {
        return ValidationError.of(clazz, field, errorType, object, null);

    }

    @Override
    public String toString() {
        if (errorType == ValidationErrorType.FIELD_IS_ILLEGALLY_NULL) {
            return "%s::%s of object %s is null, but fields of type %s should never be null!" .formatted(className, fieldName, object, fieldType);
        }

        return "%s::%s of %s-annotated object %s has following problem: %s" .formatted(className, fieldName, annotation.getSimpleName(), object, errorType.getMessage());
    }
}
