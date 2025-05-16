package net.mikka.testing.validation;

import lombok.RequiredArgsConstructor;
import net.mikka.testing.ValidationError;
import net.mikka.testing.ValidationErrorType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public abstract class AbstractTestObjectValidationStrategyBase<T extends Annotation> implements TestObjectValidationStrategy {
    private static final Class<?>[] NON_NULLABLE_CONTAINERS = {
            Optional.class,
            Collection.class,
            Map.class
    };

    protected final T testObjectMethodAnnotation;
    protected final Object testObject;

    public List<ValidationError> validateTestObjectCreatedByMethod(String testObjectCreatingMethodName) throws IllegalAccessException {
        List<ValidationError> errors = new ArrayList<>();

        final Class<?> testObjectClass = testObject.getClass();

        for (Field mustNotBeEmptyfield : getMustNotEmptyFields()) {
            mustNotBeEmptyfield.setAccessible(true);
            final Class<?> fieldType = mustNotBeEmptyfield.getType();
            final Object fieldValue = mustNotBeEmptyfield.get(testObject);

            checkIsInvalidNull(fieldType, fieldValue)
                    .or(() -> checkIsInvalidEmpty(fieldType, fieldValue))
                    .map(errorType -> ValidationError.of(testObjectCreatingMethodName, testObjectClass, mustNotBeEmptyfield, errorType, testObject, testObjectMethodAnnotation.annotationType()))
                    .ifPresent(errors::add);
        }

        for (Field mustBeEmptyfield : getMustEmptyFields()) {
            mustBeEmptyfield.setAccessible(true);
            final Class<?> fieldType = mustBeEmptyfield.getType();
            final Object fieldValue = mustBeEmptyfield.get(testObject);

            checkIsInvalidNull(fieldType, fieldValue)
                    .or(() -> checkIsInvalidNotEmpty(fieldType, fieldValue))
                    .map(errorType -> ValidationError.of(testObjectCreatingMethodName, testObjectClass, mustBeEmptyfield, errorType, testObject, testObjectMethodAnnotation.annotationType()))
                    .ifPresent(errors::add);
        }

        return errors;
    }

    private Optional<ValidationErrorType> checkIsInvalidNull(Class<?> type, Object fieldValue) {
        return isInvalidNull(type, fieldValue)
                ? Optional.of(ValidationErrorType.FIELD_IS_ILLEGALLY_NULL)
                : Optional.empty();
    }


    private Optional<ValidationErrorType> checkIsInvalidEmpty(Class<?> type, Object fieldValue) {
        return isEmpty(type, fieldValue)
                ? Optional.of(ValidationErrorType.FIELD_IS_EMPTY)
                : Optional.empty();
    }

    private Optional<ValidationErrorType> checkIsInvalidNotEmpty(Class<?> type, Object fieldValue) {
        return !isEmpty(type, fieldValue)
                ? Optional.of(ValidationErrorType.FIELD_IS_EMPTY)
                : Optional.empty();
    }

    private static boolean isInvalidNull(Class<?> type, Object fieldValue) {
        return isNonNullableContainer(type) && fieldValue == null;
    }

    protected static boolean isNonNullableContainer(Class<?> type) {
        return Arrays.stream(NON_NULLABLE_CONTAINERS).anyMatch(t -> t.isAssignableFrom(type));
    }

    private static boolean isEmpty(Class<?> type, Object fieldValue) {
        if (Collection.class.isAssignableFrom(type)) {
            Collection<?> collection = (Collection<?>) fieldValue;
            return collection.isEmpty();
        }

        if (Map.class.isAssignableFrom(type)) {
            Map<?, ?> map = (Map<?, ?>) fieldValue;
            return map.isEmpty();
        }

        if (Optional.class.isAssignableFrom(type)) {
            Optional<?> optional = (Optional<?>) fieldValue;
            return optional.isEmpty();
        }

        return fieldValue == null;
    }

    protected abstract List<Field> getMustNotEmptyFields();

    protected abstract List<Field> getMustEmptyFields();

    protected static final Predicate<Field> IS_PRIMITIVE_FIELD = field -> field.getType().isPrimitive();
    protected static final Predicate<Field> IS_NON_PRIMITIVE_FIELD = IS_PRIMITIVE_FIELD.negate();
}
