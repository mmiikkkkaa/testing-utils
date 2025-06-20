package net.mikka.testing.examples.nonnull;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Value
@Builder
@With
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Person {

    @NotNull
    String firstName;

    @Builder.Default
    Optional<String> middleName = Optional.empty();

    @NotNull
    String lastName;

    Integer age;

    int numberOfHeads;

    @Builder.Default
    List<String> hobbies = List.of();

    @Builder.Default
    Set<String> favoriteMovies = Set.of();

    @Builder.Default
    Map<Integer, String> achievementsByYear = Map.of();
}
