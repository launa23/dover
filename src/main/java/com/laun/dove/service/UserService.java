package com.laun.dove.service;

import com.laun.dove.controller.dto.UserDto;
import com.laun.dove.domain.User;
import com.laun.dove.domain.enumeration.Role;
import com.laun.dove.domain.enumeration.Status;
import com.laun.dove.exception.AppException;
import com.laun.dove.exception.ErrorCode;
import com.laun.dove.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User newUser = User.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .password(new BCryptPasswordEncoder(10).encode(user.getPassword()))
                .status(Status.ONLINE)
                .roles(new HashSet<>(List.of(Role.ROLE_USER.toString())))
                .build();
        return userRepository.save(newUser);
    }

    public User getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Optional<String> email = Optional.ofNullable(securityContext.getAuthentication().getName());
        if (email.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return userRepository.findByEmail(email.get())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public User save(User user) {
        // save user
        User storedUser = userRepository.findById(user.getId()).orElse(null);
        if (storedUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        storedUser.setStatus(Status.ONLINE);
        return userRepository.save(storedUser);
    }

    public User disconnect(User user) {
        // disconnect user
        User storedUser = userRepository.findById(user.getId()).orElse(null);
        if (storedUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        storedUser.setStatus(Status.OFFLINE);
        storedUser.setLastOnlineAt(LocalDateTime.now());
        return userRepository.save(storedUser);
    }

    public List<User> findAll() {
        // find connected users
        return userRepository.findAll();
    }

    public List<User> findAllUserOnline(String id) {
        // find connected users
        return userRepository.findByStatusAndIdIsNot(Status.ONLINE, id);
    }
}
