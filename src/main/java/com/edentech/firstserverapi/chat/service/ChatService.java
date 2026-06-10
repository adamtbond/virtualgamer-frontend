package com.edentech.firstserverapi.chat.service;

import com.edentech.firstserverapi.chat.entity.ChatMemberEntity;
import com.edentech.firstserverapi.chat.entity.ChatMemberRole;
import com.edentech.firstserverapi.chat.entity.ChatMemberStatus;
import com.edentech.firstserverapi.chat.entity.ChatRoomEntity;
import com.edentech.firstserverapi.chat.entity.ChatRoomType;
import com.edentech.firstserverapi.chat.repository.ChatMemberRepository;
import com.edentech.firstserverapi.chat.repository.ChatRoomRepository;
import com.edentech.firstserverapi.chat.dto.ChatRoomDTO;
import com.edentech.firstserverapi.chat.mapper.ChatRoomMapper;
import com.edentech.firstserverapi.entity.AppUserEntity;
import com.edentech.firstserverapi.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final AppUserRepository appUserRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatService(
            ChatRoomRepository chatRoomRepository,
            ChatMemberRepository chatMemberRepository,
            AppUserRepository appUserRepository,
            ChatRoomMapper chatRoomMapper
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.appUserRepository = appUserRepository;
        this.chatRoomMapper = chatRoomMapper;
    }

    public ChatRoomDTO createGroupChat(String groupName, String username) {
        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                .name(groupName)
                .type(ChatRoomType.GROUP)
                .createdBy(user)
                .createdAt(Instant.now())
                .build();

        ChatRoomEntity savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatMemberEntity ownerMember = ChatMemberEntity.builder()
                .chatRoom(savedChatRoom)
                .user(user)
                .role(ChatMemberRole.OWNER)
                .status(ChatMemberStatus.ACTIVE)
                .joinedAt(Instant.now())
                .build();

        chatMemberRepository.save(ownerMember);

        return chatRoomMapper.toDTO(savedChatRoom);
    }
}