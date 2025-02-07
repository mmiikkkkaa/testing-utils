package net.mikka.testing.testobjectvalidation;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TestObjectScanner {

    private static final List<Class<?>> NON_NULL_ANNOTATIONS = List.of(
            NotNull.class,
            NonNull.class,
            lombok.NonNull.class
    );

    @SneakyThrows
    public static List<String> getTestObjectValidationErrors() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(TestObjectProvider.class));

        final Set<BeanDefinition> beans = provider.findCandidateComponents("net.mikka");

        for (final BeanDefinition beanDefinition : beans) {
            final String beanClassName = beanDefinition.getBeanClassName();
            final Class<?> beanClass = ClassLoader.getSystemClassLoader().loadClass(beanClassName);

            final Method[] declaredMethods = beanClass.getDeclaredMethods();

            inspectTestObjects(declaredMethods, MinimalFilledTestObject.class, TestObjectScanner::inspectMinimalFilledTestObject);
            inspectTestObjects(declaredMethods, CompletelyFilledTestObject.class, TestObjectScanner::inspectCompletelyFilledTestObject);
        }

        // ToDo: return errors instead of exceptions
        return List.of();
    }

    private static void inspectTestObjects(
            Method[] declaredMethods,
            Class<? extends Annotation> annotation,
            Consumer<Object> inspectionMethod
    ) throws IllegalAccessException, InvocationTargetException {
        List<Method> minimalFilledTestObjectMethods = Arrays.stream(declaredMethods)
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
            inspectionMethod.accept(object);
        }
    }

    @SneakyThrows
    private static void inspectMinimalFilledTestObject(Object object) {
        System.out.println(object);
        final Class<? extends Object> minimalFilledTestObjectClass = object.getClass();


        // check fields
        final Field[] fields = minimalFilledTestObjectClass.getDeclaredFields();
        final List<Field> nonNullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(IS_TYPE_OF_NOT_NULL_FIELD)
                .toList();

        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);
            if (fieldValue == null) {
                throw new RuntimeException("value of field " + minimalFilledTestObjectClass.getName() + "::" + field.getName() + " must not be null for object " + object);
            }
        }
    }

    @SneakyThrows
    private static void inspectCompletelyFilledTestObject(Object object) {
        System.out.println(object);
        final Class<?> testObjectClass = object.getClass();

        // check fields
        final Field[] fields = testObjectClass.getDeclaredFields();
        final List<Field> nonNullFields = Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .toList();

        for (Field field : nonNullFields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(object);
            if (fieldValue == null) {
                throw new RuntimeException("value of field " + testObjectClass.getName() + "::" + field.getName() + " must not be null for object " + object);
            }
        }
    }

    private static final Predicate<Method> HAS_RETURN_TYPE = method ->
            method.getReturnType() != void.class;
    private static final Predicate<Method> HAS_NO_PARAMETERS = method ->
            method.getParameterCount() == 0;
    private static final Predicate<Method> IS_STATIC_METHOD = method ->
            Modifier.isStatic(method.getModifiers());


    private static final Predicate<Method> IS_TYPE_OF_NOT_NULL_METHOD = method ->
            Arrays.stream(method.getAnnotations())
                    .map(Annotation::getClass)
                    .anyMatch(NON_NULL_ANNOTATIONS::contains);

    private static final Predicate<Field> IS_NON_PRIMITIVE_FIELD = field ->
            !field.getType().isPrimitive();

    private static final Predicate<Field> IS_TYPE_OF_NOT_NULL_FIELD = field ->
            Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(NON_NULL_ANNOTATIONS::contains);


    private static final Predicate<Field> IS_TYPE_OF_NULLABLE_FIELD = field ->
            Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .noneMatch(NON_NULL_ANNOTATIONS::contains);
}
