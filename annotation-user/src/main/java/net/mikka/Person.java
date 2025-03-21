package net.mikka;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@With
public class Person {

    @NotNull
    private String firstName;

    @Builder.Default
    private Optional<String> middleName = Optional.empty();

    @NotNull
    private String lastName;

    private Integer age;

    @Builder.Default
    private List<String> hobbies = List.of();

    @Builder.Default
    private Set<String> favoriteMovies = Set.of();

    @Builder.Default
    private Map<Integer, String> achievementsByYear = Map.of();
}
