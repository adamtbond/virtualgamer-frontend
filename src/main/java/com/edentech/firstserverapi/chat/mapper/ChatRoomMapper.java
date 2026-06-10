package com.edentech.firstserverapi.chat.mapper;

import com.edentech.firstserverapi.chat.dto.ChatRoomDTO;
import com.edentech.firstserverapi.chat.entity.ChatRoomEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

    public ChatRoomDTO toDTO(ChatRoomEntity entity) {
        return new ChatRoomDTO(
                entity.getId(),
                entity.getName(),
                entity.getType()
        );
    }
}