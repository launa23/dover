package com.laun.dove.service;

import com.laun.dove.controller.dto.MessageDto;
import com.laun.dove.domain.ChatRoom;
import com.laun.dove.domain.Message;
import com.laun.dove.domain.User;
import com.laun.dove.exception.AppException;
import com.laun.dove.exception.ErrorCode;
import com.laun.dove.repository.ChatRoomRepository;
import com.laun.dove.repository.MessageRepository;
import com.laun.dove.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;

    public Message save(MessageDto messageDto) {
        User recipient = userRepository.findById(messageDto.getRecipientId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User currentUser = userService.getCurrentUserLogin();

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomU1AndU2(currentUser.getId(), recipient.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder().user1(currentUser).user2(recipient).build()));
        Message message = messageRepository.save(Message.builder()
                .content(messageDto.getContent())
                .recipient(recipient)
                .sender(currentUser)
                .chatRoom(chatRoom)
                .build());
        chatRoom.setLastMessage(message);
        chatRoomRepository.save(chatRoom);
        return message;
    }
}
