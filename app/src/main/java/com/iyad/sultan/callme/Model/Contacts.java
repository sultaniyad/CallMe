package com.iyad.sultan.callme.Model;

import io.realm.RealmObject;

/**
 * Created by salkhmis on 10/22/2016.
 */

public class Contacts extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String number;
}
