package com.laun.dove.controller;

import com.laun.dove.controller.dto.MessageDto;
import com.laun.dove.controller.dto.MessageProjection;
import com.laun.dove.domain.Message;
import com.laun.dove.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final MessageService messageService;

    @PostMapping("/send-message")
    public void sendMessageRest(@RequestBody MessageDto messageDto) {
        Message message = messageService.save(messageDto);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit,
                                         @RequestParam("chatRoomId") String chatRoomId) {
        log.info("Get messages");
        List<MessageProjection> messages = messageService.findByChatRoom(limit, page, chatRoomId);
        return ResponseEntity.ok(messages);
    }
}
