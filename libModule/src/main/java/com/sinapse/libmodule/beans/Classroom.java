package com.sinapse.libmodule.beans;

import java.util.List;

public class Classroom {
    private String level;
    private String name;
    private String id;
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Subject> subjects;
    private List<String> eleves;
    private List<String> professeurs;
    private long nbProf;
    private long nbEleves;
    private String docRef;


    public Classroom() {
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public long getNbProf() {
        return nbProf;
    }

    public void setNbProf(long nbProf) {
        this.nbProf = nbProf;
    }

    public long getNbEleves() {
        return nbEleves;
    }

    public void setNbEleves(long nbEleves) {
        this.nbEleves = nbEleves;
    }

    public List<String> getEleves() {
        return eleves;
    }

    public void setEleves(List<String> eleves) {
        this.eleves = eleves;
    }

    public List<String> getProfesseurs() {
        return professeurs;
    }

    public void setProfesseurs(List<String> professeurs) {
        this.professeurs = professeurs;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
