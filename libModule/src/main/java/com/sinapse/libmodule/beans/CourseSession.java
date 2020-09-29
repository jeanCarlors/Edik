package com.sinapse.libmodule.beans;

import java.util.Date;

public class CourseSession {
    private String uid;
    private String docPath;
    private Date created_date;
    private Date ended_date;
    private String status;

    public CourseSession() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getEnded_date() {
        return ended_date;
    }

    public void setEnded_date(Date ended_date) {
        this.ended_date = ended_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
