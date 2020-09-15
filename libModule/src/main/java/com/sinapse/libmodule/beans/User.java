package com.sinapse.libmodule.beans;

import java.util.Map;

public class User {
    private String uid;
    private String photo;
    private String name;
    private String type;
    private String school;
    private String status;
    private boolean verified;
    private String anneeAcademique;
    private String grade;

    public User() {
    }

    public User(Map<String, Object> data, String uid) {
        this.uid = uid;
        this.photo = (String) data.get("photo");
        this.name = (String) data.get("name");
        this.type = (String) data.get("type");
        this.school = (String) data.get("school");
        this.status = (String) data.get("status");
        this.verified = (Boolean) data.get("verified");
        this.anneeAcademique = (String) data.get("annee_academique");
        this.grade = data.containsKey("grade")? (String) data.get("grade") : null;

    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
