package net.mikka.testing.examples.nullable.failing;

import lombok.experimental.UtilityClass;
import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.NullMarkerHandling;
import net.mikka.testing.annotations.TestObjectProvider;
import net.mikka.testing.examples.nullable.Person;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
@UtilityClass
//@TestObjectProvider
public class FailingTestObjectFactory {

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static Person implicitPersonMinFailMiddleName() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static Person implicitPersonMinFailAge() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .age(28)
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static Person implicitPersonMinFailHobbies() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .hobbies(List.of("Testing"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static Person implicitPersonMinFailMovies() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static Person implicitPersonMinFailAchievements() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static Person implicitPersonMinFailHeads() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person implicitPersonMaxFailMiddleName() {
        return Person.builder()
                .firstName("John")
//                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person implicitPersonMaxFailAge() {
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
    public static Person implicitPersonMaxFailHobbies() {
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
    public static Person implicitPersonMaxFailMovies() {
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
    public static Person implicitPersonMaxFailAchievements() {
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
    public static Person implicitPersonMaxFailHeads() {
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
