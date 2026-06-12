package com.edentech.firstserverapi.chat.controller;

import com.edentech.firstserverapi.chat.dto.ChatRoomDTO;
import com.edentech.firstserverapi.chat.dto.CreateGroupChatRequest;
import com.edentech.firstserverapi.chat.service.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/groups")
    public ChatRoomDTO createGroupChat(
            @RequestBody CreateGroupChatRequest request,
            Authentication authentication
    ) {
        String username = authentication.getPrincipal().toString();
        return chatService.createGroupChat(request.name(), username);
    }
}