package com.laun.dove.service;

import com.laun.dove.controller.response.UserInChatRoom;
import com.laun.dove.domain.User;
import com.laun.dove.repository.ChatRoomRepository;
import com.laun.dove.repository.UserInChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final UserInChatRoomRepository userInChatRoomRepository;

    public List<UserInChatRoom> getUserInChatRoom() {
        User currentUser = userService.getCurrentUserLogin();
        return userInChatRoomRepository.getUserInChatRoom(currentUser.getId());
    }

}
