package net.mikka.testing;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.NullMarkerHandling;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;


@SuppressWarnings({"java:S3011", "java:S106"})
@UtilityClass
public class TestObjectScanner {

    private static final Class<?>[] NON_NULLABLE_CONTAINERS = {
            Optional.class,
            Collection.class,
            Map.class
    };

    private static final List<Class<? extends Annotation>> NON_NULL_ANNOTATIONS = List.of(
            NotNull.class,
            NonNull.class,
            lombok.NonNull.class // bringt nix, Retention=CLASS
    );

    private static final List<Class<? extends Annotation>> NULLABLE_ANNOTATIONS = List.of(
            Nullable.class
    );

    @SneakyThrows
    public static List<ValidationError> validateTestObjects(Class<?> classToTest) {
        System.out.println("Validating " + classToTest);

        List<ValidationError> errors = new ArrayList<>();

        errors.addAll(validateTestObject(classToTest, MinimalFilledTestObject.class, TestObjectScanner::validateMinimalFilledTestObject));
        errors.addAll(validateTestObject(classToTest, CompletelyFilledTestObject.class, TestObjectScanner::validateCompletelyFilledTestObject));

        return errors;
    }

    private static List<ValidationError> validateTestObject(
            Class<?> clazz,
            Class<? extends Annotation> annotation,
            TriFunction<Object, Annotation, String, List<ValidationError>> inspectionMethod
    ) throws IllegalAccessException, InvocationTargetException {

        List<ValidationError> errors = new ArrayList<>();

        List<Method> minimalFilledTestObjectMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .toList();

        minimalFilledTestObjectMethods = minimalFilledTestObjectMethods.stream()
                .filter(HAS_RETURN_TYPE)
                .filter(HAS_NO_PARAMETERS)
                .filter(IS_STATIC_METHOD)
                .toList();

        for (Method method : minimalFilledTestObjectMethods) {
            method.setAccessible(true);
            final Object object = method.invoke(null);
            errors.addAll(inspectionMethod.apply(object, method.getAnnotation(annotation), method.getName()));
        }
        return errors;
    }

    @SneakyThrows
    private static List<ValidationError> validateMinimalFilledTestObject(Object object, Annotation annotation, String methodName) {
        final Class<?> testObjectClass = object.getClass();
        System.out.println("validateMinimalFilledTestObject: " + testObjectClass.getSimpleName() + "::" + methodName);

        MinimalFilledTestObject minimalFilledAnnotation = (MinimalFilledTestObject) annotation;

        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();

        final List<Field> nonNullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(isNonNullField(minimalFilledAnnotation.nullMarkerHandling()))
                .toList();

        List<ValidationError> errors = new ArrayList<>(validateNonNullFieldsNotEmpty(object, methodName, nonNullFields, testObjectClass));

        final List<Field> nullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(isNonNullField(minimalFilledAnnotation.nullMarkerHandling()).negate())
                .toList();

        errors.addAll(validateNullableFieldsEmpty(object, methodName, nullFields, testObjectClass));
        return errors;
    }

    private static List<ValidationError> validateNullableFieldsEmpty(Object object, String methodName, List<Field> nullFields, Class<?> testObjectClass) throws IllegalAccessException {
        List<ValidationError> validationErrors = new ArrayList<>();
        for (Field field : nullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            System.out.println("validating nullable field " + field.getName() + " is empty: " + fieldValue);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_ILLEGALLY_NULL, object, MinimalFilledTestObject.class);
                validationErrors.add(validationError);
                continue; // if invalidly null, then no other things to check
            }

            if (!isEmpty(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.NULLABLE_FIELD_IS_FILLED, object, MinimalFilledTestObject.class);
                validationErrors.add(validationError);
            }
        }
        return validationErrors;
    }

    private static List<ValidationError> validateNonNullFieldsNotEmpty(Object object, String methodName, List<Field> nonNullFields, Class<?> testObjectClass) throws IllegalAccessException {
        List<ValidationError> validationErrors = new ArrayList<>();
        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            System.out.println("validating non-null field " + field.getName() + " is not empty: " + fieldValue);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_ILLEGALLY_NULL, object);
                validationErrors.add(validationError);
                continue; // if invalidly null, then no other things to check
            }

            final boolean isEmpty = isEmpty(field.getType(), fieldValue);

            if (isNonNullableContainer(field.getType()) && !isEmpty) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.NULLABLE_FIELD_IS_FILLED, object, MinimalFilledTestObject.class);
                validationErrors.add(validationError);
                continue; // skip non-nullable containers for emptiness-checks
            }

            if (isEmpty) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_EMPTY, object, MinimalFilledTestObject.class);
                validationErrors.add(validationError);
            }
        }
        return validationErrors;
    }

    private static Predicate<Field> isNonNullField(NullMarkerHandling nullMarkerHandling) {
        if (nullMarkerHandling == NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY) {
            return field -> Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(NON_NULL_ANNOTATIONS::contains);
        } else {
            return field -> Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .noneMatch(NULLABLE_ANNOTATIONS::contains);
        }
    }

    @SneakyThrows
    private static List<ValidationError> validateCompletelyFilledTestObject(Object object, Annotation annotation, String methodName) {
        final Class<?> testObjectClass = object.getClass();

        System.out.println("validateCompletelyFilledTestObject: " + testObjectClass.getSimpleName() + "::" + methodName);

        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();

        final List<Field> nonNullFields = Arrays.stream(fields)
                // TODO: log warning if primitive fields are annotated as not-null or nullable
                .filter(IS_NON_PRIMITIVE_FIELD).toList();

        List<ValidationError> errors = new ArrayList<>();
        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_ILLEGALLY_NULL, object, CompletelyFilledTestObject.class);
                errors.add(validationError);
                continue; // if invalidly null, then no other things to check
            }

            if (isEmpty(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_EMPTY, object, CompletelyFilledTestObject.class);
                errors.add(validationError);
            }
        }
        return errors;
    }

    private static boolean isInvalidNull(Class<?> type, Object fieldValue) {
        return isNonNullableContainer(type) && fieldValue == null;
    }

    private static boolean isNonNullableContainer(Class<?> type) {
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

    private static final Predicate<Method> HAS_RETURN_TYPE = method -> method.getReturnType() != void.class;
    private static final Predicate<Method> HAS_NO_PARAMETERS = method -> method.getParameterCount() == 0;
    private static final Predicate<Method> IS_STATIC_METHOD = method -> Modifier.isStatic(method.getModifiers());
    private static final Predicate<Field> IS_NON_PRIMITIVE_FIELD = field -> !field.getType().isPrimitive();
}
