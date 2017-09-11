package com.soprasteria.javazone;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import com.soprasteria.javazone.person.Person;
import com.soprasteria.javazone.product.Product;

public class SampleData {

    private Random random = new Random();
    private static final String[] lastNames = new String[] { "Brodwall", "Adams", "Smith", "Jones" };
    private static final String[] firstNames = new String[] { "Johannes", "John", "James", "Peter", "Paul" };

    public Person samplePerson() {
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

    public Person minimalPerson() {
        Person person = new Person();
        person.setFirstName(pickOne(firstNames));
        person.setMiddleName(null);
        person.setLastName(pickOne(lastNames));
        person.setDateOfBirth(null);
        return person;
    }

    public Product minimalProduct() {
        Product product = new Product();
        product.setProductName(pickOne(new String[] { "apples", "bananas", "cheese", "dumplings" }));
        return product;
    }

    public Product sampleProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setProductName(pickOne(new String[] { "apples", "bananas", "cheese", "dumplings" }));
        product.setProductCategory(pickOne(new String[] { "fruit", "dinner" }));
        product.setPriceInCents(random.nextInt(10000) * 10);
        return product;
    }

}
