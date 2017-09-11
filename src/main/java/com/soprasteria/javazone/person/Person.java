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
            .put("date-of-birth", getDateOfBirth() != null ? getDateOfBirth().toString() : null);
    }

    public static Person fromJson(JsonObject json) {
        Person person = new Person();
        person.setId(UUID.fromString(json.requiredString("id")));
        person.setFirstName(json.requiredString("first-name"));
        person.setMiddleName(json.stringValue("middle-name").orElse(null));
        person.setLastName(json.requiredString("last-name"));
        person.setDateOfBirth(json.stringValue("date-of-birth").map(LocalDate::parse).orElse(null));
        return person;
    }

}
