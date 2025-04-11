package net.mikka.testing.annotations;

public enum NullMarkerHandling {
    /**
     * fields are interpreted as nullable, as long as they are not explicitly marked as non-nullable
     */
    MARK_NON_NULLABLE_EXPLICITLY,
    /**
     * fields are interpreted as non-nullable, as long as they are not explicitly marked as nullable
     */

    MARK_NULLABLE_EXPLICITLY,
}
