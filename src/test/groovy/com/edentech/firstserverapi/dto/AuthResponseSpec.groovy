package com.edentech.firstserverapi.dto

import spock.lang.Specification

class AuthResponseSpec extends Specification {

    def "should create AuthResponse with token"() {
        given:
            String token = "jwt.token.123"

        when:
            AuthResponse response = new AuthResponse(token)

        then:
            response != null
            response.getToken() == token
    }

    def "should handle null token"() {
        when:
            AuthResponse response = new AuthResponse(null)

        then:
            response != null
            response.getToken() == null
    }

    def "should preserve token value"() {
        given:
            String token = "long.jwt.token.with.many.parts"

        when:
            AuthResponse response = new AuthResponse(token)

        then:
            response.getToken() == token
    }

    def "should handle empty token"() {
        given:
            String token = ""

        when:
            AuthResponse response = new AuthResponse(token)

        then:
            response.getToken() == ""
    }

    def "should create multiple responses with different tokens"() {
        given:
            String token1 = "token1"
            String token2 = "token2"

        when:
            AuthResponse response1 = new AuthResponse(token1)
            AuthResponse response2 = new AuthResponse(token2)

        then:
            response1.getToken() == token1
            response2.getToken() == token2
            response1.getToken() != response2.getToken()
    }
}

