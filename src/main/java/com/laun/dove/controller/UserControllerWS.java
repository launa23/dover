package com.laun.dove.controller;

import com.laun.dove.domain.User;
import com.laun.dove.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserControllerWS {
    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(@Payload User user) {
        User result = userService.save(user);
        log.info("User added: {}", result.getStatus());
        return result;
    }

    @MessageMapping("/user.disconnect")
    @SendTo("/user/public")
    public User disconnect(@Payload User user) {
        User result = userService.disconnect(user);
        log.info("User disconnected: {}", result.getStatus());
        return result;
    }
}
