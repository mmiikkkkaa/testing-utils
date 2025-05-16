package net.mikka.testing.validation;

import net.mikka.testing.annotations.CompletelyFilledTestObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class CompletelyFilledValidationStrategy extends AbstractTestObjectValidationStrategyBase<CompletelyFilledTestObject> {

    public CompletelyFilledValidationStrategy(CompletelyFilledTestObject testObjectMethodAnnotation, Object testObject) {
        super(testObjectMethodAnnotation, testObject);
    }

    @Override
    protected List<Field> getMustNotEmptyFields() {
        final Field[] fields = testObject.getClass().getDeclaredFields();
        return Arrays.stream(fields).filter(IS_NON_PRIMITIVE_FIELD).toList();
    }

    @Override
    protected List<Field> getMustEmptyFields() {
        // all fields must be field, so no empty fields exist
        return List.of();
    }
}
