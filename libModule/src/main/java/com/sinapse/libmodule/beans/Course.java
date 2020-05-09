package com.sinapse.libmodule.beans;

import java.time.LocalDateTime;

public class Course {
  private String id;
  private String subject;
  private String theme;
  private String classroom;
  private LocalDateTime courseDateTime;

    public Course() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public LocalDateTime getCourseDateTime() {
        return courseDateTime;
    }

    public void setCourseDateTime(LocalDateTime courseDateTime) {
        this.courseDateTime = courseDateTime;
    }
}
