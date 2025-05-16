package net.mikka;

import lombok.experimental.UtilityClass;
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
public class SuccessTestObjectFactory {

    public static ExplicitPerson nonAnnotatedPerson() {
        return ExplicitPerson.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .hobbies(List.of("software testing"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinSuccess() {
        return ExplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @CompletelyFilledTestObject
    public static ExplicitPerson explicitPersonMaxSuccess() {
        return ExplicitPerson.builder()
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

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinSuccess() {
        return ImplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @CompletelyFilledTestObject
    public static ImplicitPerson implicitPersonMaxSuccess() {
        return ImplicitPerson.builder()
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
