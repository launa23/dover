package com.laun.dove.domain;

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
    @Column(name = "sender_id", nullable = false)
    private User senderId;

    @ManyToOne
    @Column(name = "recipient_id", nullable = false)
    private User recipientId;

}
