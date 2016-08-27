package com.ncr.medicalconferencetracker.CustomObjects;

/**
 * Created by Mustafa on 22.08.2016.
 */
public class InviteObj {

    private String conferenceName;
    private String doctorName;
    private int id;

    public InviteObj(String conferenceName, String doctorName, int id) {
        this.conferenceName = conferenceName;
        this.doctorName = doctorName;
        this.id = id;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
