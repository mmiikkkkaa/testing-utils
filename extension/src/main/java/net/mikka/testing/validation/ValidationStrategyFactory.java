package net.mikka.testing.validation;

import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;

@UtilityClass
public class ValidationStrategyFactory {
    public static TestObjectValidationStrategy getCompletelyFilledValidationStrategy(CompletelyFilledTestObject annotation, Object objectUnderTest) {
        return new CompletelyFilledValidationStrategy(annotation, objectUnderTest);
    }

    public static TestObjectValidationStrategy getMinimallyFilledValidationStrategy(MinimalFilledTestObject annotation, Object objectUnderTest) {
        return switch (annotation.nullMarkerHandling()) {
            case MARK_NULLABLE_EXPLICITLY ->
                    new MinimalFilledExplicitNullableValidationStrategy(annotation, objectUnderTest);
            case MARK_NON_NULLABLE_EXPLICITLY ->
                    new MinimalFilledExplicitNotNullValidationStrategy(annotation, objectUnderTest);
        };
    }
}
