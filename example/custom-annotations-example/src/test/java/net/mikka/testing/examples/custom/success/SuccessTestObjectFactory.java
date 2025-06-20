package net.mikka.testing.examples.custom.success;

import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.NullMarkerHandling;
import net.mikka.testing.annotations.TestObjectProvider;
import net.mikka.testing.examples.custom.DareYouBeingNull;
import net.mikka.testing.examples.custom.Person;

import java.util.Optional;

@UtilityClass
@TestObjectProvider
@SuppressWarnings("unused")
public class SuccessTestObjectFactory {

    public static Person nonAnnotatedPerson() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }

    @CompletelyFilledTestObject
    public static Person personMax() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .build();
    }

    @MinimalFilledTestObject(
            nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY,
            nonNullAnnotations = {DareYouBeingNull.class}
    )
    public static Person personMinExplicitNonNull() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }
}
