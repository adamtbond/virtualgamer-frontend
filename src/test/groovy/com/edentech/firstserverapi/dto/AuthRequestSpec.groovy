package com.edentech.firstserverapi.dto

import spock.lang.Specification

class AuthRequestSpec extends Specification {

    def "should create AuthRequest with default constructor"() {
        when:
            AuthRequest request = new AuthRequest()

        then:
            request != null
            request.username == null
            request.password == null
    }

    def "should set and get username"() {
        given:
            AuthRequest request = new AuthRequest()

        when:
            request.setUsername("testuser")

        then:
            request.getUsername() == "testuser"
    }

    def "should set and get password"() {
        given:
            AuthRequest request = new AuthRequest()

        when:
            request.setPassword("password123")

        then:
            request.getPassword() == "password123"
    }

    def "should set both username and password"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("john")
            request.setPassword("secret")

        when:
            String username = request.getUsername()
            String password = request.getPassword()

        then:
            username == "john"
            password == "secret"
    }

    def "should handle null values"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername(null)
            request.setPassword(null)

        when:
            String username = request.getUsername()
            String password = request.getPassword()

        then:
            username == null
            password == null
    }

    def "should handle empty strings"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("")
            request.setPassword("")

        when:
            String username = request.getUsername()
            String password = request.getPassword()

        then:
            username == ""
            password == ""
    }

    def "should allow updating credentials"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("initial")
            request.setPassword("pass1")

        when:
            request.setUsername("updated")
            request.setPassword("pass2")

        then:
            request.getUsername() == "updated"
            request.getPassword() == "pass2"
    }
}

