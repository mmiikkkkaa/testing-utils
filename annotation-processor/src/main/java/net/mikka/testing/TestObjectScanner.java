package net.mikka.testing;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.TestObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

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
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;


@SuppressWarnings({"java:S3011", "java:S106"})
public class TestObjectScanner {
    private TestObjectScanner() {
        // static class
    }

    private static final List<Class<?>> INVALID_NULLABLE_TYPES = List.of(
            Optional.class,
            Collection.class,
            Map.class
    );

    private static final List<Class<?>> NON_NULL_ANNOTATIONS = List.of(
            NotNull.class,
            NonNull.class,
            lombok.NonNull.class // bringt nix, Retention=CLASS
    );

    @SneakyThrows
    public static List<ValidationError> validateTestObjects() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(TestObjectProvider.class));

        final Set<BeanDefinition> beans = provider.findCandidateComponents("net.mikka");

        List<ValidationError> errors = new ArrayList<>();
        for (final BeanDefinition beanDefinition : beans) {
            final String beanClassName = beanDefinition.getBeanClassName();
            final Class<?> beanClass = ClassLoader.getSystemClassLoader().loadClass(beanClassName);

            errors.addAll(validateTestObject(beanClass, MinimalFilledTestObject.class, TestObjectScanner::validateMinimalFilledTestObject));
            errors.addAll(validateTestObject(beanClass, CompletelyFilledTestObject.class, TestObjectScanner::validateCompletelyFilledTestObject));
        }

        return errors;
    }

    @SneakyThrows
    public static List<ValidationError> validateTestObjects(Class<?> classToTest) {

        List<ValidationError> errors = new ArrayList<>();

        errors.addAll(validateTestObject(classToTest, MinimalFilledTestObject.class, TestObjectScanner::validateMinimalFilledTestObject));
        errors.addAll(validateTestObject(classToTest, CompletelyFilledTestObject.class, TestObjectScanner::validateCompletelyFilledTestObject));

        return errors;
    }

    private static List<ValidationError> validateTestObject(
            Class<?> clazz,
            Class<? extends Annotation> annotation,
            Function<Object, List<ValidationError>> inspectionMethod
    ) throws IllegalAccessException, InvocationTargetException {

        List<ValidationError> errors = new ArrayList<>();

        List<Method> minimalFilledTestObjectMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .toList();

        // Verify the return an object
        minimalFilledTestObjectMethods = minimalFilledTestObjectMethods.stream()
                .filter(HAS_RETURN_TYPE)
                .filter(HAS_NO_PARAMETERS)
                .filter(IS_STATIC_METHOD)
                .toList();

        for (Method method : minimalFilledTestObjectMethods) {
            method.setAccessible(true);
            final Object object = method.invoke(null);
            errors.addAll(inspectionMethod.apply(object));
        }
        return errors;
    }

    @SneakyThrows
    private static List<ValidationError> validateMinimalFilledTestObject(Object object) {
        List<ValidationError> errors = new ArrayList<>();
        final Class<?> testObjectClass = object.getClass();


        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();
        final List<Field> nonNullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(IS_TYPE_OF_NOT_NULL_FIELD.negate())
                .toList();

        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(
                        testObjectClass,
                        field,
                        ValidationErrorType.FIELD_IS_ILLEGALLY_NULL,
                        object
                );
                errors.add(validationError);
                continue; // if invalid null, then no other things to check
            }
            if (!isEmpty(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(
                        testObjectClass,
                        field,
                        ValidationErrorType.NULLABLE_FIELD_IS_FILLED,
                        object,
                        MinimalFilledTestObject.class
                );
                errors.add(validationError);
            }
        }
        return errors;
    }

    @SneakyThrows
    private static List<ValidationError> validateCompletelyFilledTestObject(Object object) {
        List<ValidationError> errors = new ArrayList<>();
        final Class<?> testObjectClass = object.getClass();

        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();
        final List<Field> nonNullFields = Arrays.stream(fields)
                // TODO: log warning if primitive fields are annotated as not-null or nullable
                .filter(IS_NON_PRIMITIVE_FIELD)
                .toList();


        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (isInvalidNull(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(
                        testObjectClass,
                        field,
                        ValidationErrorType.FIELD_IS_ILLEGALLY_NULL,
                        object
                );
                errors.add(validationError);
                continue; // if invalid null, then no other things to check
            }

            if (isEmpty(field.getType(), fieldValue)) {
                ValidationError validationError = ValidationError.of(
                        testObjectClass,
                        field,
                        ValidationErrorType.FIELD_IS_EMPTY,
                        object,
                        CompletelyFilledTestObject.class
                );
                errors.add(validationError);
            }
        }
        return errors;
    }

    private static boolean isInvalidNull(Class<?> type, Object fieldValue) {
        return INVALID_NULLABLE_TYPES.stream()
                .filter(t -> t.isAssignableFrom(type))
                .findAny()
                .map(it -> fieldValue == null)
                .orElse(false);
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

    private static final Predicate<Method> HAS_RETURN_TYPE = method ->
            method.getReturnType() != void.class;
    private static final Predicate<Method> HAS_NO_PARAMETERS = method ->
            method.getParameterCount() == 0;
    private static final Predicate<Method> IS_STATIC_METHOD = method ->
            Modifier.isStatic(method.getModifiers());

    private static final Predicate<Field> IS_NON_PRIMITIVE_FIELD = field ->
            !field.getType().isPrimitive();

    private static final Predicate<Field> IS_TYPE_OF_NOT_NULL_FIELD = field ->
            Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(NON_NULL_ANNOTATIONS::contains);
}
