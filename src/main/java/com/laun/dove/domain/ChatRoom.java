package com.laun.dove.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_2", nullable = false)
    private User user2;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;

}
