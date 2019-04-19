package com.example.healthkit;

public class User_Information {
    private String name,area,patientUid;

    public User_Information(String name, String location, String uid) {
        this.name = name;
        this.area = location;
        this.patientUid = uid;
    }

    public User_Information(String name, String location) {
        this.name = name;
        this.area = location;
    }
    public User_Information(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }
}
