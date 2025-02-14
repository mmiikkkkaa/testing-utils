package net.mikka.testing.testobjectvalidation;

import lombok.SneakyThrows;
import net.mikka.tutorial.TestObjectFactory;
import net.mikka.tutorial.domain.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TestObjectScannerTest {

    private static final Method inspectMinimalFilledTestObjectMethod = getTestObjectScannerMethod("inspectMinimalFilledTestObject");
    private static final Method inspectCompletelyFilledTestObjectMethod = getTestObjectScannerMethod("inspectCompletelyFilledTestObject");

    @SuppressWarnings("unchecked")
    @Nested
    class FullyFilledTestObjectTests {

        @Test
        void should_accept_fully_filled_test_objects() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMax();

            // when
            final List<?> validationErrors = (List<?>) inspectCompletelyFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(0, validationErrors.size());
        }

        @Test
        void should_reject_null_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMax()
                    .withFirstName(null);

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectCompletelyFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "firstName", ValidationErrorType.FIELD_IS_EMPTY);
        }

        @Test
        void should_reject_empty_Optional_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMax()
                    .withMiddleName(Optional.empty()); // empty is not fully filled

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectCompletelyFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "middleName", ValidationErrorType.FIELD_IS_EMPTY);
        }

        @Test
        void should_reject_empty_collection_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMax()
                    .withHobbies(List.of()); // empty is not fully filled

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectCompletelyFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "hobbies", ValidationErrorType.FIELD_IS_EMPTY);
        }

        @Test
        void should_reject_empty_map_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMax()
                    .withAchievementsByYear(Map.of()); // empty is not fully filled

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectCompletelyFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "achievementsByYear", ValidationErrorType.FIELD_IS_EMPTY);
        }
    }

    @SuppressWarnings("unchecked")
    @Nested
    class MinimalFilledTestObjectTests {

        @Test
        void should_accept_minimal_filled_test_objects() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMin();

            // when
            final List<?> validationErrors = (List<?>) inspectMinimalFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(0, validationErrors.size());
        }

        @Test
        void should_reject_filled_fields_without_notNull_annotation() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMin()
                    .withAge(28); // nullable field

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectMinimalFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "age", ValidationErrorType.NULLABLE_FIELD_IS_FILLED);
        }

        @Test
        void should_reject_non_empty_optional_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMin()
                    .withMiddleName(Optional.of("Wayne")); // optionals should not be filled

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectMinimalFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "middleName", ValidationErrorType.NULLABLE_FIELD_IS_FILLED);
        }

        @Test
        void should_reject_null_optional_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMin()
                    .withMiddleName(null); // optionals should be empty, but not null

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectMinimalFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "middleName", ValidationErrorType.FIELD_IS_ILLEGALLY_NULL);
        }

        @Test
        void should_reject_non_empty_collection_fields() throws InvocationTargetException, IllegalAccessException {
            // given
            final Person person = TestObjectFactory.personMin()
                    .withHobbies(List.of("Testing")); // collections should not be filled

            // when
            final List<ValidationError> validationErrors = (List<ValidationError>) inspectMinimalFilledTestObjectMethod.invoke(null, person);

            // then
            Assertions.assertEquals(1, validationErrors.size());
            assertError(validationErrors.get(0), "hobbies", ValidationErrorType.NULLABLE_FIELD_IS_FILLED);
        }

    }
    @SneakyThrows
    private static Method getTestObjectScannerMethod(String methodName) {
        final Method verifyCompletelyFilledObject = TestObjectScanner.class.getDeclaredMethod(methodName, Object.class);
        verifyCompletelyFilledObject.setAccessible(true);
        return verifyCompletelyFilledObject;
    }

    private static void assertError(ValidationError validationError, String fieldName, ValidationErrorType errorType) {
        Assertions.assertEquals(fieldName, validationError.getFieldName());
        Assertions.assertEquals(errorType, validationError.getErrorType());
    }
}