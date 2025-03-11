package com.laun.dove.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnoreProperties(value = {"fullName", "email", "status", "lastOnlineAt", "roles", "createdAt", "updatedAt", "deletedAt", "deleted"}, allowSetters = true)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @JsonIgnoreProperties(value = {"fullName", "email", "status", "lastOnlineAt", "roles", "createdAt", "updatedAt", "deletedAt", "deleted"}, allowSetters = true)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    @JsonIgnoreProperties(value = {"user1", "user2", "lastMessage", "createdAt", "updatedAt", "deletedAt", "deleted"}, allowSetters = true)
    private ChatRoom chatRoom;
}
