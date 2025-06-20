package net.mikka.testing.examples.nonnull.success;

import lombok.experimental.UtilityClass;
import net.mikka.testing.examples.nonnull.Person;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.NullMarkerHandling;
import net.mikka.testing.annotations.TestObjectProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@UtilityClass
@TestObjectProvider
@SuppressWarnings("unused")
public class SuccessTestObjectFactory {

    public static Person nonAnnotatedPerson() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .hobbies(List.of("software testing"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinSuccess() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @CompletelyFilledTestObject
    public static Person explicitPersonMaxSuccess() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }
}
