package net.mikka.testing.examples.custom;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Optional;

@Value
@Builder
@With
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Person {

    @DareYouBeingNull
    String firstName;

    @Builder.Default
    Optional<String> middleName = Optional.empty();

    @DareYouBeingNull
    String lastName;

    Integer age;
}
