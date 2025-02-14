package net.mikka.tutorial;

import net.mikka.testing.testobjectvalidation.CompletelyFilledTestObject;
import net.mikka.testing.testobjectvalidation.MinimalFilledTestObject;
import net.mikka.testing.testobjectvalidation.TestObjectScanner;
import net.mikka.testing.testobjectvalidation.ValidationError;
import net.mikka.tutorial.domain.Person;
import net.mikka.testing.testobjectvalidation.TestObjectProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@TestObjectProvider
public class TestObjectFactory {

    private TestObjectFactory() {
        // static
    }

    // ToDo: auto-generated method, if class is annotated with TestObjectProvider
    @Test
    void shouldHaveValidTestObjects() {
      // given
        final List<ValidationError> validationErrors = TestObjectScanner.validateTestObjects();
        Assertions.assertEquals(0, validationErrors.size());
    }

    @MinimalFilledTestObject
    public static Person personMin() {
        return Person.builder()
                .firstName("John")
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
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }
}
