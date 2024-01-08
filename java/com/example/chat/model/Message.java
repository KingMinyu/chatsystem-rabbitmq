package com.example.chat.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Message {
	private String sender;
    private String content;
    // Getters and Setters

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}