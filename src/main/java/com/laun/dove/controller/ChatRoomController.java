package com.laun.dove.controller;

import com.laun.dove.controller.response.UserInChatRoom;
import com.laun.dove.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/chat-room")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/get-user-in-chat-room")
    public List<UserInChatRoom> getUserInChatRoom(@RequestParam("limit") int limit, @RequestParam("page") int page) {
        return chatRoomService.getUserInChatRoom(limit, page);
    }
}
