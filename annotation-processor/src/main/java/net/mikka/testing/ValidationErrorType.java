package net.mikka.testing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorType {
    FIELD_IS_EMPTY("field is empty, but should be filled"),
    NULLABLE_FIELD_IS_FILLED("field is not empty, even though it is not required"),
    FIELD_IS_ILLEGALLY_NULL("type that should NEVER be null is null");

    private final String message;
}
