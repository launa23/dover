package com.laun.dove.controller.response;

import lombok.*;

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
}
