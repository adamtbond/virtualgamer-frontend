package com.edentech.firstserverapi.dto

import spock.lang.Specification

class AppUserDTOSpec extends Specification {

    def "should create AppUserDTO with default constructor"() {
        when:
            AppUserDTO dto = new AppUserDTO()

        then:
            dto != null
            dto.id == null
            dto.username == null
    }

    def "should create AppUserDTO with parameters"() {
        given:
            Long id = 1L
            String username = "testuser"

        when:
            AppUserDTO dto = new AppUserDTO(id, username)

        then:
            dto.id == id
            dto.username == username
    }

    def "should set and get id"() {
        given:
            AppUserDTO dto = new AppUserDTO()

        when:
            dto.setId(5L)

        then:
            dto.getId() == 5L
    }

    def "should set and get username"() {
        given:
            AppUserDTO dto = new AppUserDTO()

        when:
            dto.setUsername("john_doe")

        then:
            dto.getUsername() == "john_doe"
    }

    def "should handle null username"() {
        given:
            AppUserDTO dto = new AppUserDTO(1L, null)

        when:
            String username = dto.getUsername()

        then:
            username == null
    }

    def "should handle null id"() {
        given:
            AppUserDTO dto = new AppUserDTO(null, "username")

        when:
            Long id = dto.getId()

        then:
            id == null
    }

    def "should allow updating fields"() {
        given:
            AppUserDTO dto = new AppUserDTO(1L, "initial")

        when:
            dto.setId(2L)
            dto.setUsername("updated")

        then:
            dto.getId() == 2L
            dto.getUsername() == "updated"
    }

    def "should handle empty string username"() {
        given:
            AppUserDTO dto = new AppUserDTO(1L, "")

        when:
            String username = dto.getUsername()

        then:
            username == ""
    }

    def "should create multiple DTOs independently"() {
        when:
            AppUserDTO dto1 = new AppUserDTO(1L, "user1")
            AppUserDTO dto2 = new AppUserDTO(2L, "user2")

        then:
            dto1.getId() == 1L
            dto1.getUsername() == "user1"
            dto2.getId() == 2L
            dto2.getUsername() == "user2"
    }

    def "should not expose password field"() {
        given:
            AppUserDTO dto = new AppUserDTO(1L, "testuser")

        when:
            String dtoString = dto.toString()

        then:
            !dtoString.contains("password")
    }
}

