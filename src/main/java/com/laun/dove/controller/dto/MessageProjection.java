package com.laun.dove.controller.dto;

public interface MessageProjection {
    String getId();
    String getContent();
    String getCreatedAt();
    String getChatRoomId();
    String getSenderId();
    String getRecipientId();
    boolean getDeleted();
}
