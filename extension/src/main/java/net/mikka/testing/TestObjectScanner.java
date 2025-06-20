package net.mikka.testing;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.validation.ValidationStrategyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


@SuppressWarnings({"java:S3011", "java:S106"})
@UtilityClass
public class TestObjectScanner {

    @SneakyThrows
    @SuppressWarnings("unused")
    public static List<ValidationError> validateTestObjects(Class<?> classToTest) {
        System.out.println("Validating " + classToTest);

        List<ValidationError> errors = new ArrayList<>();

        errors.addAll(validateTestObjectProvider(classToTest, MinimalFilledTestObject.class, TestObjectScanner::validateMinimalFilledTestObject));
        errors.addAll(validateTestObjectProvider(classToTest, CompletelyFilledTestObject.class, TestObjectScanner::validateCompletelyFilledTestObject));

        return errors;
    }

    private static List<ValidationError> validateTestObjectProvider(
            Class<?> clazz,
            Class<? extends Annotation> annotation,
            TriFunction<Object, Annotation, String, List<ValidationError>> inspectionMethod
    ) throws IllegalAccessException, InvocationTargetException {

        List<ValidationError> errors = new ArrayList<>();

        List<Method> testObjectMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .toList();

        testObjectMethods = testObjectMethods.stream()
                .filter(HAS_RETURN_TYPE)
                .filter(HAS_NO_PARAMETERS)
                .filter(IS_STATIC_METHOD) // ToDo: müssen die static sein?
                .toList();

        for (Method method : testObjectMethods) {
            method.setAccessible(true);
            final Object object = method.invoke(null);

            errors.addAll(inspectionMethod.apply(object, method.getAnnotation(annotation), method.getName()));
        }
        return errors;
    }

    @SneakyThrows
    private static List<ValidationError> validateMinimalFilledTestObject(Object testObject, Annotation testObjectMethodAnnotation, String testObjectCreatingMethodName) {
        return ValidationStrategyFactory
                .getMinimalllyFilledValidationStrategy((MinimalFilledTestObject) testObjectMethodAnnotation, testObject)
                .validateTestObjectCreatedByMethod(testObjectCreatingMethodName);
    }

    @SneakyThrows
    private static List<ValidationError> validateCompletelyFilledTestObject(Object testObject, Annotation testObjectMethodAnnotation, String testObjectCreatingMethodName) {
        return ValidationStrategyFactory
                .getCompletyFilledValidationStrategy((CompletelyFilledTestObject) testObjectMethodAnnotation, testObject)
                .validateTestObjectCreatedByMethod(testObjectCreatingMethodName);
    }

    private static final Predicate<Method> HAS_RETURN_TYPE = method -> method.getReturnType() != void.class;
    private static final Predicate<Method> HAS_NO_PARAMETERS = method -> method.getParameterCount() == 0;
    private static final Predicate<Method> IS_STATIC_METHOD = method -> Modifier.isStatic(method.getModifiers());
}
