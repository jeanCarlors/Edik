package com.sinapse.libmodule;

import java.util.List;

public class Teacher {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private List<Subject> Subjects;
    private List<String> classroom;
    private String profile;

    public Teacher() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Subject> getSubjects() {
        return Subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        Subjects = subjects;
    }

    public List<String> getClassroom() {
        return classroom;
    }

    public void setClassroom(List<String> classroom) {
        this.classroom = classroom;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
