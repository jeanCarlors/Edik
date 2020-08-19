package com.sinapse.direction.ui.Models;

public class Topic {
    private String topic;
    private String path;


    public Topic(String topic, String path) {
        this.topic = topic;
        this.path = path;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
