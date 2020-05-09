package com.sinapse.libmodule;

import java.time.LocalDateTime;
import java.util.List;

public class Assignment {
    private Subject subject;
    private String assignmentContent;
    private List<String> links;
    private List<String> documents;
    private LocalDateTime assignmentIssueDate;
    private LocalDateTime assignmentDueDate;

    public Assignment() {

    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getAssignmentContent() {
        return assignmentContent;
    }

    public void setAssignmentContent(String assignmentContent) {
        this.assignmentContent = assignmentContent;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public LocalDateTime getAssignmentIssueDate() {
        return assignmentIssueDate;
    }

    public void setAssignmentIssueDate(LocalDateTime assignmentIssueDate) {
        this.assignmentIssueDate = assignmentIssueDate;
    }

    public LocalDateTime getAssignmentDueDate() {
        return assignmentDueDate;
    }

    public void setAssignmentDueDate(LocalDateTime assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }
}
