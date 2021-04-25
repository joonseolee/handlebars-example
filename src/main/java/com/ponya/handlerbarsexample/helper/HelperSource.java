package com.ponya.handlerbarsexample.helper;

import com.ponya.handlerbarsexample.model.Person;

public class HelperSource {

    public String isBusy(Person context) {
        String busyString = context.isBusy() ? "busy" : "available";
        return context.getName() + " - " + busyString;
    }
}
