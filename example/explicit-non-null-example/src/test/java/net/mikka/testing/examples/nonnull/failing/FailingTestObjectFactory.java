package net.mikka.testing.examples.nonnull.failing;

import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.NullMarkerHandling;
import net.mikka.testing.annotations.TestObjectProvider;
import net.mikka.testing.examples.nonnull.Person;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
@UtilityClass
//@TestObjectProvider
public class FailingTestObjectFactory {

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinFailMiddleName() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinFailAge() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .age(28)
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinFailHobbies() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .hobbies(List.of("Testing"))
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinFailMovies() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinFailAchievements() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person explicitPersonMinFailHeads() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person explicitPersonMaxFailMiddleName() {
        return Person.builder()
                .firstName("John")
//                .middleName(Optional.of("Wayne")) // missing
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person explicitPersonMaxFailAge() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
//                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person explicitPersonMaxFailHobbies() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
//                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person explicitPersonMaxFailMovies() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
//                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }


    @CompletelyFilledTestObject
    public static Person explicitPersonMaxFailAchievements() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
//                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person explicitPersonMaxFailHeads() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
//                .numberOfHeads(1)
                .build();
    }
}
