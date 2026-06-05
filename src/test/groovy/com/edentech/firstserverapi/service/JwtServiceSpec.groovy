package com.edentech.firstserverapi.service

import spock.lang.Specification
import spock.lang.Unroll

class JwtServiceSpec extends Specification {

    JwtService jwtService

    def setup() {
        jwtService = new JwtService()
    }

    def "should generate a valid JWT token with username as subject"() {
        given:
            String username = "testuser"

        when:
            String token = jwtService.generateToken(username)

        then:
            token != null
            token.contains(".")
            token.split("\\.").length == 3
    }

    def "should extract username from valid token"() {
        given:
            String username = "johndoe"
            String token = jwtService.generateToken(username)

        when:
            String extractedUsername = jwtService.extractUsername(token)

        then:
            extractedUsername == username
    }

    def "should return true for valid token"() {
        given:
            String username = "alice"
            String token = jwtService.generateToken(username)

        when:
            boolean isValid = jwtService.isTokenValid(token)

        then:
            isValid == true
    }

    def "should return false for invalid token"() {
        given:
            String invalidToken = "invalid.token.here"

        when:
            boolean isValid = jwtService.isTokenValid(invalidToken)

        then:
            isValid == false
    }

    def "should return false for malformed token"() {
        given:
            String malformedToken = "not-a-valid-jwt"

        when:
            boolean isValid = jwtService.isTokenValid(malformedToken)

        then:
            isValid == false
    }

    def "should return false for empty token"() {
        given:
            String emptyToken = ""

        when:
            boolean isValid = jwtService.isTokenValid(emptyToken)

        then:
            isValid == false
    }

    @Unroll
    def "should generate different tokens for different usernames: #username"() {
        given:
            String username1 = "user1"
            String username2 = "user2"

        when:
            String token1 = jwtService.generateToken(username1)
            String token2 = jwtService.generateToken(username2)

        then:
            token1 != token2

        where:
            username << ["user1", "user2", "testadmin"]
    }

    def "should throw exception when extracting username from invalid token"() {
        given:
            String invalidToken = "invalid.fake.token"

        when:
            jwtService.extractUsername(invalidToken)

        then:
            thrown(Exception)
    }
}

