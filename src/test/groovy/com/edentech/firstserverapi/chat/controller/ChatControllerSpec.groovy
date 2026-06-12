package com.edentech.firstserverapi.chat.controller

import com.edentech.firstserverapi.chat.dto.ChatRoomDTO
import com.edentech.firstserverapi.chat.dto.CreateGroupChatRequest
import com.edentech.firstserverapi.chat.entity.ChatRoomType
import com.edentech.firstserverapi.chat.service.ChatService
import org.springframework.security.core.Authentication
import spock.lang.Specification

class ChatControllerSpec extends Specification {

    ChatService chatService = Mock()
    Authentication authentication = Mock()

    ChatController chatController = new ChatController(chatService)

    def "should create group chat for authenticated user"() {
        given:
        def request = new CreateGroupChatRequest("Friday Night Games")
        def expectedResponse = new ChatRoomDTO(
                10L,
                "Friday Night Games",
                ChatRoomType.GROUP
        )

        authentication.getPrincipal() >> "adam"

        when:
        def result = chatController.createGroupChat(request, authentication)

        then:
        1 * chatService.createGroupChat("Friday Night Games", "adam") >> expectedResponse

        result == expectedResponse
        result.id() == 10L
        result.name() == "Friday Night Games"
        result.type() == ChatRoomType.GROUP
    }
}