package com.example.chat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageForm {
    private String content;
    private String roomId;
    private String sender;
    // Getter and Setter
}