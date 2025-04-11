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

        List<ValidationError> errors = new ArrayList<>();

        errors.addAll(validateTestObject(classToTest, MinimalFilledTestObject.class, TestObjectScanner::validateMinimalFilledTestObject));
        errors.addAll(validateTestObject(classToTest, CompletelyFilledTestObject.class, TestObjectScanner::validateCompletelyFilledTestObject));

        return errors;
    }

    private static List<ValidationError> validateTestObject(
            Class<?> clazz, Class<? extends Annotation> annotation, TriFunction<Object, Annotation, String, List<ValidationError>> inspectionMethod) throws IllegalAccessException, InvocationTargetException {

        List<ValidationError> errors = new ArrayList<>();

        List<Method> minimalFilledTestObjectMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.getAnnotation(annotation) != null).toList();

        // Verify the return an object
        minimalFilledTestObjectMethods = minimalFilledTestObjectMethods.stream().filter(HAS_RETURN_TYPE).filter(HAS_NO_PARAMETERS).filter(IS_STATIC_METHOD).toList();

        for (Method method : minimalFilledTestObjectMethods) {
//            System.out.println("testing " + method.getName());
            final Annotation methodAnnotation = method.getAnnotation(annotation);
            method.setAccessible(true);
            final Object object = method.invoke(null);
            errors.addAll(inspectionMethod.apply(object, methodAnnotation, method.getName()));
        }
        return errors;
    }

    @SneakyThrows
    private static List<ValidationError> validateMinimalFilledTestObject(Object object, Annotation annotation, String methodName) {
        List<ValidationError> errors = new ArrayList<>();
        final Class<?> testObjectClass = object.getClass();
        final MinimalFilledTestObject minimalFilledTestObject = (MinimalFilledTestObject) annotation;

        Predicate<Field> isNonNullField;
        if (minimalFilledTestObject.nullMarkerHandling() == NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY) {
            isNonNullField = field -> Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(NON_NULL_ANNOTATIONS::contains);
        } else {
            isNonNullField = field -> Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .noneMatch(NULLABLE_ANNOTATIONS::contains);
        }

        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();

        final List<Field> nonNullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(isNonNullField) // minimal: so everything not marked as non-nullable must be null
                .toList();

        for (Field field : nonNullFields) {
//            System.out.println("field to be assumed to be non null: " + field.getName());

            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_ILLEGALLY_NULL, object);
                errors.add(validationError);
                continue; // if invalid null, then no other things to check
            }
            if (isEmpty(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.NULLABLE_FIELD_IS_FILLED, object, MinimalFilledTestObject.class);
                errors.add(validationError);
            }

        }

        final List<Field> nullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(isNonNullField.negate()) // minimal: so everything not marked as non-nullable must be null
                .toList();

        for (Field field : nullFields) {
//            System.out.println("field to be assumed to be null: " + field.getName());

            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_ILLEGALLY_NULL, object);
                errors.add(validationError);
                continue; // if invalid null, then no other things to check
            }
            if (!isEmpty(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.NULLABLE_FIELD_IS_FILLED, object, MinimalFilledTestObject.class);
                errors.add(validationError);
            }

        }
        return errors;
    }

    @SneakyThrows
    private static List<ValidationError> validateCompletelyFilledTestObject(Object object, Annotation annotation, String methodName) {
        List<ValidationError> errors = new ArrayList<>();
        final Class<?> testObjectClass = object.getClass();
//        final CompletelyFilledTestObject completelyFilledTestObject = (CompletelyFilledTestObject) annotation;

        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();


        // TODO: unterscheiden zwischen explizit nullable und explizit nicht-nullable

        final List<Field> nonNullFields = Arrays.stream(fields)
                // TODO: log warning if primitive fields are annotated as not-null or nullable
                .filter(IS_NON_PRIMITIVE_FIELD).toList();

        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(methodName, testObjectClass, field, ValidationErrorType.FIELD_IS_ILLEGALLY_NULL, object);
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
        return Arrays.stream(NON_NULLABLE_CONTAINERS).filter(t -> t.isAssignableFrom(type)).findAny().map(it -> fieldValue == null).orElse(false);
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
