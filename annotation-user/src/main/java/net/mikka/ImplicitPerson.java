package net.mikka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.lang.Nullable;

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
public class ImplicitPerson {

    private String firstName;

    //    @Nullable
    @Builder.Default
    private Optional<String> middleName = Optional.empty();

    private String lastName;

    @Nullable
    private Integer age;

    @Nullable
    private Integer numberOfHeads;

    @Nullable
    @Builder.Default
    private List<String> hobbies = List.of();

    @Nullable
    @Builder.Default
    private Set<String> favoriteMovies = Set.of();

    @Nullable
    @Builder.Default
    private Map<Integer, String> achievementsByYear = Map.of();
}
