package com.laun.dove.service;

import com.laun.dove.controller.dto.MessageDto;
import com.laun.dove.domain.Message;
import com.laun.dove.domain.User;
import com.laun.dove.exception.AppException;
import com.laun.dove.exception.ErrorCode;
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

    public Message save(MessageDto messageDto) {
        User user = userRepository.findById(messageDto.getRecipientId())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        Message message = Message.builder()
                .content(messageDto.getContent())
                .recipientId(user)
                .senderId(userService.getCurrentUserLogin())
                .build();
        return messageRepository.save(message);
    }
}
