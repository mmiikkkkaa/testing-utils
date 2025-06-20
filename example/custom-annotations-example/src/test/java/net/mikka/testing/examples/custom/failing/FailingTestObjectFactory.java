package net.mikka.testing.examples.custom.failing;

import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.NullMarkerHandling;
import net.mikka.testing.annotations.TestObjectProvider;
import net.mikka.testing.examples.custom.DareYouBeingNull;
import net.mikka.testing.examples.custom.Person;

import java.util.Optional;

@SuppressWarnings("unused")
@UtilityClass
//@TestObjectProvider
public class FailingTestObjectFactory {

    @MinimalFilledTestObject(
            nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY,
            nonNullAnnotations = {DareYouBeingNull.class}
    )
    public static Person personMinExplicitNonNullFailMiddleName() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }
}
