package com.ncr.medicalconferencetracker.CustomObjects;

/**
 * Created by Mustafa on 21.08.2016.
 */
public class TopicObj {

    private String name;
    private String description;
    private String ownerName;
    private int id;

    public TopicObj(String name, String description, String ownerName, int id) {
        this.name = name;
        this.description = description;
        this.ownerName = ownerName;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
