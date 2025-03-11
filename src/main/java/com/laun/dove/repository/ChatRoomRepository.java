package com.laun.dove.repository;

import com.laun.dove.controller.dto.UserDto;
import com.laun.dove.controller.response.UserInChatRoom;
import com.laun.dove.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Query(value = "SELECT * FROM chat_rooms c WHERE (c.user_1 = :user1 AND c.user_2 = :user2) OR (c.user_1 = :user2 AND c.user_2 = :user1)", nativeQuery = true)
    Optional<ChatRoom> findByChatRoomU1AndU2(@Param("user1") String user1, @Param("user2") String user2);

    @Query( value = "select DISTINCT\n" +
            "\tcase \n" +
            "\t\twhen cr.user_1 = :userId then cr.user_2\n" +
            "\t\tELSE cr.user_1\n" +
            "\tend as user_id,\n" +
            "\tcr.updated_at,\n" +
            "\tu.fullname,\n" +
            "\tu.email,\n" +
            "\tu.status,\n" +
            "\tm.content,\n" +
            "\tm.sender_id\n" +
            "from chat_rooms cr\n" +
            "inner join users u ON (u.id = cr.user_1 or u.id = cr.user_2)\n" +
            "\tinner join messages m on m.id = cr.last_message_id\n" +
            "where (cr.user_1 = :userId OR cr.user_2 = :userId)\n" +
            "and u.id != :userId", nativeQuery = true)
    List<UserInChatRoom> getUserInChatRoom(@Param("userId") String userId);
}
