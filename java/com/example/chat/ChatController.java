package com.example.chat;


import com.example.chat.model.Message;
import com.example.chat.model.MessageForm;
import com.example.chat.model.MessageListForm;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageForm messageForm) {
        String content = messageForm.getContent();
        messagingTemplate.convertAndSend("/topic/waiting", new Message("Anonymous", content));
    }
    @GetMapping("/")
    public String getChatPage(Model model) {
        model.addAttribute("messageForm", new MessageForm());
        return "index";
    }
    @GetMapping("/getRooms")
    public String show(Model model) {

    	model.addAttribute("messageForm", new MessageForm());
        // HTML 페이지 이름 반환
        return "chat";
    }
    @MessageMapping("/chat/{roomId}")
    public void handleChatMessage(@Payload MessageForm messageForm, @DestinationVariable("roomId") String roomId) {
    	String content = messageForm.getContent();
    	String uerrname = messageForm.getSender();
    	messagingTemplate.convertAndSend("/topic/" + roomId, new Message(uerrname,content));
    }
}