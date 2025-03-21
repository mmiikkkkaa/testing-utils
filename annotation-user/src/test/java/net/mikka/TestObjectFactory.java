package net.mikka;

import net.mikka.testing.annotations.CompletelyFilledTestObject;
import net.mikka.testing.annotations.MinimalFilledTestObject;
import net.mikka.testing.annotations.TestObjectProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@TestObjectProvider
public class TestObjectFactory {

    private TestObjectFactory() {
        // static
    }

    @MinimalFilledTestObject
    public static Person personMin() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
//                .hobbies(List.of("software testing")) // should cause error
                .build();
    }

    @CompletelyFilledTestObject
    public static Person personMax() {
        return Person.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }
}
