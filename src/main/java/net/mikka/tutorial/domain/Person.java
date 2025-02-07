package net.mikka.tutorial.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Person {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    // ToDo: throw exception in minimalfilled
//    @NotNull
    private Integer age;

    private List<String> hobbies;
}
