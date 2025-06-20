# Testing Utils

Annotations helping to check the quality of test data.

## Motivation

In some projects I've seen test data of bad quality, e.g. test objects without required values, which caused tests to
fail, since some mappers were expecting this data. But instead fixing the test data, some developers changed the
production code, e.g. by making mappers handling required data as nullable. To avoid such types of worsening code via
tests, this project should help
checking test data quality.

## Installation

Add the following dependency to your Maven pom:

```
<dependency>
  <groupId>net.mikka.testing</groupId>
  <artifactId>testing-utils</artifactId>
  <version>0.1.0-SNAPSHOT</version>
  <scope>test</scope>
</dependency>
```

## Features

The testing utils provide two main features, activated by annotations:

* checking a test object is completely filled, so all attributes are present. This should ensure that e.g. newly added
  attributes cannot be forgotten in mapper tests.
* checking a test object is minimally filled, so all required data is present, but not more than that. This should
  ensure that mappers can be tested for being able to handle reasonably non-provided data.

These test data is checked via generated tests, which will fail if an annotated test object is not completely
respectively minimally filled. "Filled" in this case means, that

* non-primitive attributes are set to a different value then `null`
* `Collection` and `Map` attributes contain entries
* `Optional` attributes are not empty

In any case, `Collection`, `Map` and `Optional` attributes are always expected to not be null. In minimal filled contexts they
are expected to be empty.

## Usage

A class, which provides test data objects, must be annotated with `@TestObjectProvider`.
Within that class, methods that provide those test data objects, can be annotated with either
`@CompletelyFilledTestObject` or `@MinimalFilledTestObject`.

Given a class is defined as following (example with lombok to reduce manually written code):
```java
@Value
@Builder
@With
public class Person {

  @NotNull
  String firstName;

  @Builder.Default
  Optional<String> middleName = Optional.empty();

  @NotNull
  String lastName;

  Integer age;
  
  @Builder.Default
  List<String> hobbies = List.of();

  @Builder.Default
  Set<String> favoriteMovies = Set.of();

  @Builder.Default
  Map<Integer, String> achievementsByYear = Map.of();
}
```

This classes defines `firstName` and `lastName` to be required by `@NotNull` annotation, everything else is nullable. 

With this class definition, a corresponding test object factory class can be annotated as following:

```java
@TestObjectProvider
public class TestObjectFactory {
    
    @MinimalFilledTestObject(nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY)
    public static Person personMin() {
        return ExplicitPerson.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }

    @CompletelyFilledTestObject
    public static ExplicitPerson personMax() {
        return ExplicitPerson.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .hobbies(List.of("Testing"))
                .favoriteMovies(Set.of("Sharknado", "Sharknado 2"))
                .achievementsByYear(Map.of(2025, "Developing Testing-Utils"))
                .build();
    }
}
```

In this example a generated test would fail, since `personMin` has the not required value `middleName` defined, and `personMax` is missing the `age` attribute :
```log
org.opentest4j.AssertionFailedError: The following problems were found in TestObjectFactory: 
Person::middleName of MinimalFilledTestObject-annotated object personMin (Person(firstName=John, middleName=Optional[Wayne], lastName=Doe, age=null,hobbies=[], favoriteMovies=[], achievementsByYear={})) has following problem: field is not empty, even though it is not required
Person::age of CompletelyFilledTestObject-annotated object personMax (Person(firstName=John, middleName=Optional[Wayne], lastName=Doe, age=null, hobbies=[Testing], favoriteMovies=[Sharknado 2, Sharknado], achievementsByYear={2025=Developing Testing-Utils})) has following problem: field is empty, but should be filled
```

The `@MinimalFilledTestObject` annotation has two ways of handling the nullable declaration, using its `nullMarkerHandling` parameter:
* `NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY` expects classes to explicitly specify __non__-nullable values by appropriate annotations 
* `NullMarkerHandling.MARK_NULLABLE_EXPLICITLY` expects classes to explicitly specify nullable values by appropriate annotations

In any case, annotations must be used, that are available at runtime. Compile-time-only annotations are obviously **not** working.

Via the `nonNullAnnotations` respectively `nullableAnnotations` parameters, suitable annotations can be specified, also custom annotations can be used:   


```java

    @MinimalFilledTestObject(
            nullMarkerHandling = NullMarkerHandling.MARK_NON_NULLABLE_EXPLICITLY,
            nonNullAnnotations = {DareYouBeingNull.class})
    public static ExplicitPerson person() {
        return ExplicitPerson.builder()
                .firstName("John")
                .middleName(Optional.of("Wayne"))
                .lastName("Doe")
                .build();
    }
```

Per default the following annotations are considered:

nonNullAnnotations:
* `jakarta.validation.constraints.NotNull`
* `org.springframework.lang.NonNull`

nullableAnnotations:
* `org.springframework.lang.Nullable`