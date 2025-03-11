package com.laun.dove.repository;

import com.laun.dove.controller.dto.MessageProjection;
import com.laun.dove.domain.ChatRoom;
import com.laun.dove.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = """
                SELECT m.id as id, 
                       m.content as content, 
                       m.created_at as createdAt, 
                       m.chat_room_id as chatRoomId, 
                       m.sender_id as senderId, 
                       m.recipient_id as recipientId, 
                       m.is_deleted as deleted
                FROM messages m
                where m.chat_room_id = :chatRoomId
                order by createdAt desc limit :limit offset :offset""", nativeQuery = true)
    List<MessageProjection> findByMessageInChatRoom(@Param("chatRoomId") String chatRoomId, @Param("limit") int limit, @Param("offset") int offset);
}
