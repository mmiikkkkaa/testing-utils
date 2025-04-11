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
public class TestObjectFactory {

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMin() {
        return ExplicitPerson.builder()
                .firstName("John")
//                .middleName(Optional.of("Wrong")) // explicit empty is fine
//                .middleName(null) // optionals should never be null
                .lastName("Doe")
//                .hobbies(List.of("software testing")) // hobbies are not required
                .build();
    }

    @CompletelyFilledTestObject
    public static ExplicitPerson explicitPersonMax() {
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
    public static ImplicitPerson implicitPersonMin() {
        return ImplicitPerson.builder()
                .firstName("John")
//                .middleName(Optional.empty()) // explicit empty is fine
//                .middleName(null) // optionals should never be null
                .lastName("Doe")
//                .hobbies(List.of("software testing")) // hobbies are not required
                .build();
    }

    @CompletelyFilledTestObject
    public static ImplicitPerson implicitPersonMax() {
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
