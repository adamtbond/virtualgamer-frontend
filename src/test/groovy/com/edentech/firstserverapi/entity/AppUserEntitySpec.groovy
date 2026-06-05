package com.edentech.firstserverapi.entity

import spock.lang.Specification

class AppUserEntitySpec extends Specification {

    def "should create AppUserEntity with default constructor"() {
        when:
            AppUserEntity user = new AppUserEntity()

        then:
            user != null
            user.username == null
            user.password == null
            user.id == null
    }

    def "should create AppUserEntity with parameters"() {
        given:
            String username = "testuser"
            String password = "password123"

        when:
            AppUserEntity user = new AppUserEntity(username, password)

        then:
            user.username == username
            user.password == password
    }

    def "should set and get username"() {
        given:
            AppUserEntity user = new AppUserEntity()

        when:
            user.setUsername("john_doe")

        then:
            user.getUsername() == "john_doe"
    }

    def "should set and get password"() {
        given:
            AppUserEntity user = new AppUserEntity()

        when:
            user.setPassword("secure_pass")

        then:
            user.getPassword() == "secure_pass"
    }

    def "should set and get id"() {
        given:
            AppUserEntity user = new AppUserEntity("user", "pass")

        when:
            user.id = 5L

        then:
            user.getId() == 5L
    }

    def "should handle null values in constructor"() {
        when:
            AppUserEntity user = new AppUserEntity(null, null)

        then:
            user.username == null
            user.password == null
    }

    def "should allow updating username"() {
        given:
            AppUserEntity user = new AppUserEntity("initial", "pass")

        when:
            user.setUsername("updated")

        then:
            user.getUsername() == "updated"
    }

    def "should allow updating password"() {
        given:
            AppUserEntity user = new AppUserEntity("user", "initial_pass")

        when:
            user.setPassword("updated_pass")

        then:
            user.getPassword() == "updated_pass"
    }

    def "should preserve id"() {
        given:
            AppUserEntity user = new AppUserEntity("user", "pass")
            user.id = 10L

        when:
            Long userId = user.getId()

        then:
            userId == 10L
    }

    def "should create multiple users independently"() {
        when:
            AppUserEntity user1 = new AppUserEntity("user1", "pass1")
            user1.id = 1L
            AppUserEntity user2 = new AppUserEntity("user2", "pass2")
            user2.id = 2L

        then:
            user1.username == "user1"
            user1.password == "pass1"
            user1.getId() == 1L
            user2.username == "user2"
            user2.password == "pass2"
            user2.getId() == 2L
    }

    def "should handle empty string values"() {
        when:
            AppUserEntity user = new AppUserEntity("", "")

        then:
            user.username == ""
            user.password == ""
    }
}

