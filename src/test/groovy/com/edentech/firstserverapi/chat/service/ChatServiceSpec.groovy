package com.edentech.firstserverapi.chat.service

import com.edentech.firstserverapi.chat.entity.ChatMemberRole
import com.edentech.firstserverapi.chat.entity.ChatMemberStatus
import com.edentech.firstserverapi.chat.entity.ChatRoomType
import com.edentech.firstserverapi.chat.repository.ChatMemberRepository
import com.edentech.firstserverapi.chat.repository.ChatRoomRepository
import com.edentech.firstserverapi.chat.dto.ChatRoomDTO
import com.edentech.firstserverapi.chat.mapper.ChatRoomMapper
import com.edentech.firstserverapi.entity.AppUserEntity
import com.edentech.firstserverapi.repository.AppUserRepository
import spock.lang.Specification

class ChatServiceSpec extends Specification {

    ChatRoomRepository chatRoomRepository = Mock()
    ChatMemberRepository chatMemberRepository = Mock()
    AppUserRepository appUserRepository = Mock()
    ChatRoomMapper chatRoomMapper = new ChatRoomMapper()

    ChatService chatService = new ChatService(
            chatRoomRepository,
            chatMemberRepository,
            appUserRepository,
            chatRoomMapper)

    def "createGroupChat creates group room and owner membership"() {
        given:
        def username = "adam"
        def groupName = "Friday Night Games"

        def user = new AppUserEntity()
        user.setUsername(username)

        appUserRepository.findByUsername(username) >> Optional.of(user)

        when:
        def result = chatService.createGroupChat(groupName, username)

        then:
        1 * chatRoomRepository.save({
            it.name == groupName &&
                    it.type == ChatRoomType.GROUP &&
                    it.createdBy == user
        }) >> { args ->
            args[0].setId(10L)
            return args[0]
        }

        1 * chatMemberRepository.save({
            it.chatRoom.id == 10L &&
                    it.user == user &&
                    it.role == ChatMemberRole.OWNER &&
                    it.status == ChatMemberStatus.ACTIVE
        })

        result instanceof ChatRoomDTO
        result.id() == 10L
        result.name() == groupName
        result.type() == ChatRoomType.GROUP
    }
}