package com.edentech.firstserverapi.chat.repository;

import com.edentech.firstserverapi.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
}
