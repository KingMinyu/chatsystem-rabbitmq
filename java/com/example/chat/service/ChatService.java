package com.example.chat.service;

import com.example.chat.model.ChatMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(RabbitTemplate rabbitTemplate, SimpMessagingTemplate messagingTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/chat", message);
        rabbitTemplate.convertAndSend("chat-exchange", "chat-routing-key", message);
    }
}