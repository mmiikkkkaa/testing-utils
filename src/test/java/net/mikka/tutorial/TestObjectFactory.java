package net.mikka.tutorial;

import net.mikka.testing.testobjectvalidation.CompletelyFilledTestObject;
import net.mikka.testing.testobjectvalidation.MinimalFilledTestObject;
import net.mikka.testing.testobjectvalidation.TestObjectScanner;
import net.mikka.tutorial.domain.Person;
import net.mikka.testing.testobjectvalidation.TestObjectProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@TestObjectProvider
public class TestObjectFactory {

    private TestObjectFactory() {
        // static
    }

    // ToDo: automatic generated method, if class is annotated with TestObjectProvider
    @Test
    void shouldHaveValidTestObjects() {
      // given
        final List<String> validationErrors = TestObjectScanner.getTestObjectValidationErrors();
        Assertions.assertEquals(0, validationErrors.size());
    }

    @MinimalFilledTestObject
    public static Person personMin() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .age(99)
                .build();
    }

    @CompletelyFilledTestObject
    public static Person personMax() {
        return Person.builder()
                .firstName("John")
                .lastName("Doe")
                .age(28)
                .hobbies(List.of("Fooling"))
                .build();
    }
}
