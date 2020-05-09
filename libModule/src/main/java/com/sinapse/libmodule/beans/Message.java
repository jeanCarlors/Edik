package com.sinapse.libmodule.beans;

import java.time.LocalDateTime;
import java.util.Set;

public class Message {
    private String sender;
    private Set<String> receiver;
    private LocalDateTime messageDateTime;
    private String content;
    private Set<String> attachments;

    public Message() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Set<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(Set<String> receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(LocalDateTime messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<String> attachments) {
        this.attachments = attachments;
    }
}
