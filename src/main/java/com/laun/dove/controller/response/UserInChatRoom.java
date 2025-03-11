package com.laun.dove.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInChatRoom {
    private String id;
    private String fullName;
    private String content;
    private String senderId;
    private String status;
    private String chatRoomId;
    private LocalDateTime lastOnlineAt;
    private LocalDateTime updatedAt;
}
