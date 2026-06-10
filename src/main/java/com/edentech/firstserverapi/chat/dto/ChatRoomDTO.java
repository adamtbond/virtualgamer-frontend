package com.edentech.firstserverapi.chat.dto;

import com.edentech.firstserverapi.chat.entity.ChatRoomType;

public record ChatRoomDTO(
        Long id,
        String name,
        ChatRoomType type
) {
}