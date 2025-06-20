package net.mikka.testing;

import lombok.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Value
public class ValidationError {
    String objectName;
    String className;
    String fieldName;
    String fieldType;
    ValidationErrorType errorType;
    String object;
    Class<? extends Annotation> annotation;

    public static ValidationError of(String objectName, Class<?> clazz, Field field, ValidationErrorType errorType, Object object, Class<? extends Annotation> annotation) {
        return new ValidationError(
                objectName,
                clazz.getName(),
                field.getName(),
                field.getType().getName(),
                errorType,
                object.toString(),
                annotation
        );
    }

    public static ValidationError of(String objectName, Class<?> clazz, Field field, ValidationErrorType errorType, Object object) {
        return ValidationError.of(objectName, clazz, field, errorType, object, null);

    }

    @Override
    public String toString() {
        if (errorType == ValidationErrorType.FIELD_IS_ILLEGALLY_NULL) {
            return "%s::%s of object %s (%s) is null, but fields of type %s should never be null!" .formatted(className, fieldName, objectName, object, fieldType);
        }

        return "%s::%s of %s-annotated object %s (%s) has following problem: %s" .formatted(className, fieldName, annotation.getSimpleName(), objectName, object, errorType.getMessage());
    }
}
