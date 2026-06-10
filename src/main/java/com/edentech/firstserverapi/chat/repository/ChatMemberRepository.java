package com.edentech.firstserverapi.chat.repository;

import com.edentech.firstserverapi.chat.entity.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, Long> {
}