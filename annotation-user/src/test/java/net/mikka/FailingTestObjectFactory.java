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
//@TestObjectProvider
public class FailingTestObjectFactory {

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinFailMiddleName() {
        return ExplicitPerson.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinFailAge() {
        return ExplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .age(28)
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinFailHobbies() {
        return ExplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .hobbies(List.of("Testing"))
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinFailMovies() {
        return ExplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinFailAchievements() {
        return ExplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }


    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static ExplicitPerson explicitPersonMinFailHeads() {
        return ExplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static ExplicitPerson explicitPersonMaxFailMiddleName() {
        return ExplicitPerson.builder()
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
    public static ExplicitPerson explicitPersonMaxFailAge() {
        return ExplicitPerson.builder()
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
    public static ExplicitPerson explicitPersonMaxFailHobbies() {
        return ExplicitPerson.builder()
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
    public static ExplicitPerson explicitPersonMaxFailMovies() {
        return ExplicitPerson.builder()
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
    public static ExplicitPerson explicitPersonMaxFailAchievements() {
        return ExplicitPerson.builder()
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
    public static ExplicitPerson explicitPersonMaxFailHeads() {
        return ExplicitPerson.builder()
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

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinFailMiddleName() {
        return ImplicitPerson.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinFailAge() {
        return ImplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .age(28)
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinFailHobbies() {
        return ImplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .hobbies(List.of("Testing"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinFailMovies() {
        return ImplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinFailAchievements() {
        return ImplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }

    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NULLABLE_EXPLICITLY)
    public static ImplicitPerson implicitPersonMinFailHeads() {
        return ImplicitPerson.builder()
                .firstName("John")
                .lastName("Doe")
                .numberOfHeads(1)
                .build();
    }

    @CompletelyFilledTestObject
    public static ImplicitPerson implicitPersonMaxFailMiddleName() {
        return ImplicitPerson.builder()
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
    public static ImplicitPerson implicitPersonMaxFailAge() {
        return ImplicitPerson.builder()
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
    public static ImplicitPerson implicitPersonMaxFailHobbies() {
        return ImplicitPerson.builder()
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
    public static ImplicitPerson implicitPersonMaxFailMovies() {
        return ImplicitPerson.builder()
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
    public static ImplicitPerson implicitPersonMaxFailAchievements() {
        return ImplicitPerson.builder()
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
    public static ImplicitPerson implicitPersonMaxFailHeads() {
        return ImplicitPerson.builder()
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
