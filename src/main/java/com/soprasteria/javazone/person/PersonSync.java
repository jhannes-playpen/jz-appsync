package com.soprasteria.javazone.person;

public class PersonSync {

    private PersonRepository serverRepo;
    private PersonRepository clientRepo;

    public PersonSync(PersonRepository serverRepo, PersonRepository clientRepo) {
        this.serverRepo = serverRepo;
        this.clientRepo = clientRepo;
    }

    public void doSync() {
        for (Person person : serverRepo.list()) {
            clientRepo.save(person);
        }
    }

}
