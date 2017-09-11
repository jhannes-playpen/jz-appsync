package com.soprasteria.javazone;

import java.time.LocalDate;
import java.util.Random;

import com.soprasteria.javazone.person.Person;

public class SampleData {

    private Random random = new Random();

    public Person samplePerson() {
        String[] lastNames = new String[] { "Brodwall", "Adams", "Smith", "Jones" };

        Person person = new Person();
        person.setFirstName(pickOne(new String[] { "Johannes", "John", "James", "Peter", "Paul" }));
        person.setMiddleName(pickOne(lastNames));
        person.setLastName(pickOne(lastNames));
        person.setDateOfBirth(randomBirthDate());
        return person;
    }

    private LocalDate randomBirthDate() {
        return LocalDate.now().minusYears(20).minusDays(random.nextInt(365*30));
    }

    private <T> T pickOne(T[] strings) {
        return strings[random.nextInt(strings.length)];
    }

}
