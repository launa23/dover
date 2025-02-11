package com.laun.dove.controller;

import com.laun.dove.controller.response.ApiResponse;
import com.laun.dove.controller.dto.UserDto;
import com.laun.dove.domain.User;
import com.laun.dove.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody @Valid UserDto user) {

        ApiResponse<User> response = new ApiResponse<>();
        response.setData(userService.createUser(user));
        return ResponseEntity.ok(response);
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser( @Payload User user) {
        userService.save(user);
        return user;
    }

    @MessageMapping("/user.disconnect")
    @SendTo("/user/topic")
    public User disconnect(@Payload User user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
}
