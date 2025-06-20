package net.mikka.testing.validation;

import net.mikka.testing.annotations.MinimalFilledTestObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MinimalFilledExplicitNotNullValidationStrategy extends AbstractTestObjectValidationStrategyBase<MinimalFilledTestObject> {

    public MinimalFilledExplicitNotNullValidationStrategy(MinimalFilledTestObject testObjectMethodAnnotation, Object testObject) {
        super(testObjectMethodAnnotation, testObject);
    }

    @Override
    protected List<Field> getMustNotEmptyFields() {
        final Field[] fields = testObject.getClass().getDeclaredFields();

        return Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(isNonNullField())
                .toList();
    }

    @Override
    protected List<Field> getMustEmptyFields() {
        final Field[] fields = testObject.getClass().getDeclaredFields();

        return Arrays.stream(fields)
                .filter(IS_NON_PRIMITIVE_FIELD)
                .filter(isNullField())
                .toList();
    }

    private Predicate<Field> isNonNullField() {
        return field -> {
            if (isNonNullableContainer(field.getType())) {
                return false;
            }
            return Arrays.stream(field.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(it -> Arrays.asList(this.testObjectMethodAnnotation.nonNullAnnotations()).contains(it));
        };
    }

    private Predicate<Field> isNullField() {
        return isNonNullField().negate();
    }
}
