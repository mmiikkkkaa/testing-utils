package net.mikka.testing.testobjectvalidation;

import lombok.Getter;

@Getter
public class InvalidNullException extends RuntimeException {
    private final String className;
    private final String fieldName;
    private final String object;

    public InvalidNullException(String className, String fieldName, String object) {
        this.className = className;
        this.fieldName = fieldName;
        this.object = object;
    }
}
