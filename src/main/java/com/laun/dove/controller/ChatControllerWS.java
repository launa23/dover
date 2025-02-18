package com.laun.dove.controller;

import com.laun.dove.controller.dto.MessageDto;
import com.laun.dove.domain.Message;
import com.laun.dove.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatControllerWS {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDto messageDto) {
        Message message = messageService.save(messageDto);
        messagingTemplate.convertAndSendToUser(messageDto.getRecipientId(), "/queue/messages", message);
    }

    @PostMapping("/send-message")
    public void sendMessageRest(@RequestBody MessageDto messageDto) {
        Message message = messageService.save(messageDto);
    }
}
