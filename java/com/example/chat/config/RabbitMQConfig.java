package com.example.chat.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue chatQueue() {
        return new Queue("chatQueue");
    }
}
/*
 * 
const socket = new SockJS('/ws-chat');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    stompClient.subscribe('/topic/messages', function (response) {
        const message = JSON.parse(response.body);
        // 메시지를 화면에 표시하는 로직 추가
    });
});

function sendMessage(content, sender) {
    const message = { content, sender };
    stompClient.send("/app/chat", {}, JSON.stringify(message));
}
 * */