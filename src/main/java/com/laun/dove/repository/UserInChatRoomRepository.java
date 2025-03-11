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

    public List<UserInChatRoom> getUserInChatRoom(String userId, int limit, int page) {
        int offset = limit * (page - 1);
        String sql = """ 
                select DISTINCT 
                    case 
                        when cr.user_1 = ? then cr.user_2
                        ELSE cr.user_1
                    end as user_id,
                    cr.updated_at,
                    u.fullname,
                    cr.id as chat_room_id,
                    u.email,
                    u.last_online_at,
                    m.content,
                    u.status,
                    m.sender_id
                from chat_rooms cr
                inner join users u ON (u.id = cr.user_1 or u.id = cr.user_2)
                inner join messages m on m.id = cr.last_message_id
                where (cr.user_1 = ? OR cr.user_2 = ?)
                and u.id != ?
                order by cr.updated_at desc
                limit ? offset ? """;
        return jdbcTemplate.query(sql, new Object[]{userId, userId, userId, userId, limit, offset}, (rs, rowNum) -> UserInChatRoom.builder()
                .id(rs.getString("user_id"))
                .fullName(rs.getString("fullname"))
                .content(rs.getString("content"))
                .senderId(rs.getString("sender_id"))
                .status(rs.getString("status"))
                .chatRoomId(rs.getString("chat_room_id"))
                .lastOnlineAt(rs.getTimestamp("last_online_at") == null ? null : rs.getTimestamp("last_online_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build());
    }
}
