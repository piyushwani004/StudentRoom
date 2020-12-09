package com.piyush004.studentroom.Room.Topic;

public class TopicModel {

    private String TopicName;

    public TopicModel() {
    }

    public TopicModel(String topicName) {
        TopicName = topicName;
    }

    public String getTopicName() {
        return TopicName;
    }

    public void setTopicName(String topicName) {
        TopicName = topicName;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "TopicName='" + TopicName + '\'' +
                '}';
    }
}
