package com.ncr.medicalconferencetracker.CustomObjects;

/**
 * Created by Mustafa on 21.08.2016.
 */
public class ConferenceObj {

    private String name;
    private String description;
    private int id;

    public ConferenceObj(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }
}
