package com.edentech.firstserverapi.mapper

import com.edentech.firstserverapi.dto.AppUserDTO
import com.edentech.firstserverapi.entity.AppUserEntity
import spock.lang.Specification

class AppUserMapperSpec extends Specification {

    def "should convert AppUserEntity to AppUserDTO"() {
        given:
            AppUserEntity user = new AppUserEntity("testuser", "password123")
            user.id = 1L

        when:
            AppUserDTO dto = AppUserMapper.toDTO(user)

        then:
            dto != null
            dto.id == 1L
            dto.username == "testuser"
    }

    def "should not expose password in DTO"() {
        given:
            AppUserEntity user = new AppUserEntity("admin", "secretpassword")
            user.id = 2L

        when:
            AppUserDTO dto = AppUserMapper.toDTO(user)

        then:
            dto.username == "admin"
            dto.id == 2L
            // Password should not be in the DTO
            !dto.toString().contains("secretpassword")
    }

    def "should convert AppUserDTO to AppUserEntity"() {
        given:
            AppUserDTO dto = new AppUserDTO(1L, "johndoe")

        when:
            AppUserEntity user = AppUserMapper.toEntity(dto)

        then:
            user != null
            user.username == "johndoe"
            user.password == null
    }

    def "should handle null AppUserEntity in toDTO"() {
        when:
            AppUserDTO dto = AppUserMapper.toDTO(null)

        then:
            dto == null
    }

    def "should handle null AppUserDTO in toEntity"() {
        when:
            AppUserEntity user = AppUserMapper.toEntity(null)

        then:
            user == null
    }

    def "should convert AppUserEntity with null values"() {
        given:
            AppUserEntity user = new AppUserEntity(null, null)
            user.id = 5L

        when:
            AppUserDTO dto = AppUserMapper.toDTO(user)

        then:
            dto != null
            dto.id == 5L
            dto.username == null
    }

    def "should preserve username during mapping"() {
        given:
            String username = "alice@example.com"
            AppUserEntity user = new AppUserEntity(username, "encrypted")
            user.id = 10L

        when:
            AppUserDTO dto = AppUserMapper.toDTO(user)

        then:
            dto.username == username
            dto.id == 10L
    }

    def "should create new DTO instance"() {
        given:
            AppUserEntity user = new AppUserEntity("user1", "pass")
            user.id = 1L

        when:
            AppUserDTO dto1 = AppUserMapper.toDTO(user)
            AppUserDTO dto2 = AppUserMapper.toDTO(user)

        then:
            dto1 != dto2
            dto1.id == dto2.id
            dto1.username == dto2.username
    }

    def "should set username when converting DTO to entity"() {
        given:
            AppUserDTO dto = new AppUserDTO(3L, "newuser")

        when:
            AppUserEntity user = AppUserMapper.toEntity(dto)

        then:
            user.username == "newuser"
            user.password == null
    }
}

