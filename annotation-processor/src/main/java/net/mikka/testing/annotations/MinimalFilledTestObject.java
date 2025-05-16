package net.mikka.testing.annotations;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MinimalFilledTestObject {
    NullMarkerHandling nullMarkerHandling() default NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY;

    Class<? extends Annotation>[] nullableAnnotations() default {
            Nullable.class
    };

    Class<? extends Annotation>[] nonNullAnnotations() default {
            NotNull.class,
            NonNull.class,
            lombok.NonNull.class // bringt nix, Retention=CLASS
    };
}
