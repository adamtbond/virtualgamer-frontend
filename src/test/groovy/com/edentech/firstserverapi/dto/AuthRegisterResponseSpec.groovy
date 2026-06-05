package com.edentech.firstserverapi.dto

import spock.lang.Specification

class AuthRegisterResponseSpec extends Specification {

    def "should create AuthRegisterResponse with parameters"() {
        given:
            String message = "User registered"
            Long userId = 1L

        when:
            AuthRegisterResponse response = new AuthRegisterResponse(message, userId)

        then:
            response != null
            response.message == message
            response.userId == userId
    }

    def "should set and get message"() {
        given:
            AuthRegisterResponse response = new AuthRegisterResponse("Test", 1L)

        when:
            String message = response.message

        then:
            message == "Test"
    }

    def "should set and get userId"() {
        given:
            AuthRegisterResponse response = new AuthRegisterResponse("Registered", 5L)

        when:
            Long userId = response.userId

        then:
            userId == 5L
    }

    def "should handle null message"() {
        when:
            AuthRegisterResponse response = new AuthRegisterResponse(null, 1L)

        then:
            response != null
            response.message == null
            response.userId == 1L
    }

    def "should handle null userId"() {
        when:
            AuthRegisterResponse response = new AuthRegisterResponse("User registered", null)

        then:
            response != null
            response.message == "User registered"
            response.userId == null
    }

    def "should handle both null values"() {
        when:
            AuthRegisterResponse response = new AuthRegisterResponse(null, null)

        then:
            response != null
            response.message == null
            response.userId == null
    }

    def "should preserve message and userId values"() {
        given:
            String message = "Registration successful"
            Long userId = 42L

        when:
            AuthRegisterResponse response = new AuthRegisterResponse(message, userId)

        then:
            response.message == message
            response.userId == userId
    }

    def "should create multiple responses independently"() {
        when:
            AuthRegisterResponse response1 = new AuthRegisterResponse("User 1 registered", 1L)
            AuthRegisterResponse response2 = new AuthRegisterResponse("User 2 registered", 2L)

        then:
            response1.message == "User 1 registered"
            response1.userId == 1L
            response2.message == "User 2 registered"
            response2.userId == 2L
    }
}

