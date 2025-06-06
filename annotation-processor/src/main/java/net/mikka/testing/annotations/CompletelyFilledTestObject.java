package net.mikka.testing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CompletelyFilledTestObject {
    NullMarkerHandling nullMarkerHandling() default NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY;
}
