package com.example.chat;

import com.example.chat.model.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RabbitMQListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "chatQueue")
    public void handleMessageFromRabbitMQ(Message message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}