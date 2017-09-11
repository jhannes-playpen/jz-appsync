package com.soprasteria.javazone.person;

import java.time.LocalDate;
import java.util.UUID;

import org.jsonbuddy.JsonObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"firstName", "middleName", "lastName", "dateOfBirth" })
public class Person {

    @Getter @Setter
    private UUID id;

    @Getter @Setter
    private String firstName, middleName, lastName;

    @Getter @Setter
    private LocalDate dateOfBirth;

    public JsonObject toJson() {
        return new JsonObject()
            .put("id", getId().toString())
            .put("first-name", getFirstName())
            .put("middle-name", getMiddleName())
            .put("last-name", getLastName())
            .put("date-of-birth", getDateOfBirth().toString());
    }

    public static Person fromJson(JsonObject json) {
        Person person = new Person();
        person.setId(UUID.fromString(json.requiredString("id")));
        person.setFirstName(json.requiredString("first-name"));
        person.setMiddleName(json.requiredString("middle-name"));
        person.setLastName(json.requiredString("last-name"));
        person.setDateOfBirth(LocalDate.parse(json.requiredString("date-of-birth")));
        return person;
    }

}
