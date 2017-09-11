package com.soprasteria.javazone.person;

public class PersonRepositoryTest {

    @Test
    public void shouldRetrieveSavedPerson() {
        Person person = samplePerson();
        assertThat(person).hasNoNullFieldsOrProperties();
        repository.save(person);

        assertThat(repository.retrieve(person.getId()))
            .isEqualToComparingFieldByField(person);
    }

}
