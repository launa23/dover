package com.laun.dove.repository;

import com.laun.dove.controller.response.UserInChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserInChatRoomRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<UserInChatRoom> getUserInChatRoom(String userId) {
        String sql = "select DISTINCT case \n" +
                "\t\twhen cr.user_1 = ? then cr.user_2\n" +
                "\t\tELSE cr.user_1\n" +
                "\tend as user_id,\n" +
                "\tcr.updated_at,\n" +
                "\tu.fullname,\n" +
                "\tu.email,\n" +
                "\tm.content,\n" +
                "\tm.sender_id\n" +
                "from chat_rooms cr\n" +
                "inner join users u ON (u.id = cr.user_1 or u.id = cr.user_2)\n" +
                "\tinner join messages m on m.id = cr.last_message_id\n" +
                "where (cr.user_1 = ? OR cr.user_2 = ?)\n" +
                "and u.id != ?";
        return jdbcTemplate.query(sql, new Object[]{userId, userId, userId, userId}, (rs, rowNum) -> UserInChatRoom.builder()
                .id(rs.getString("user_id"))
                .fullName(rs.getString("fullname"))
                .content(rs.getString("content"))
                .senderId(rs.getString("sender_id"))
                .build());
    }
}
