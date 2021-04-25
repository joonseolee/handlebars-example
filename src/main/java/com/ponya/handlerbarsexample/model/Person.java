package com.ponya.handlerbarsexample.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Person {

    private String name;
    private boolean busy;
    private Address address = new Address();
    private List<Person> friends = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    @Getter
    @Setter
    public static class Address {
        private String street;
    }
}
