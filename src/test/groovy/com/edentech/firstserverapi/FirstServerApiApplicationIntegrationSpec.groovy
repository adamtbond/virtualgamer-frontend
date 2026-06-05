package com.edentech.firstserverapi

import com.edentech.firstserverapi.controller.HelloController
import com.edentech.firstserverapi.controller.AuthController
import com.edentech.firstserverapi.controller.NoteController
import com.edentech.firstserverapi.service.JwtService
import com.edentech.firstserverapi.repository.AppUserRepository
import com.edentech.firstserverapi.repository.NoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

@SpringBootTest
class FirstServerApiApplicationIntegrationSpec extends Specification {

    @Autowired(required = false)
    HelloController helloController

    @Autowired(required = false)
    AuthController authController

    @Autowired(required = false)
    NoteController noteController

    @Autowired(required = false)
    JwtService jwtService

    @Autowired(required = false)
    AppUserRepository appUserRepository

    @Autowired(required = false)
    NoteRepository noteRepository

    @Autowired(required = false)
    PasswordEncoder passwordEncoder

    def "should load application context"() {
        expect:
            helloController != null
            authController != null
            noteController != null
            jwtService != null
    }

    def "should inject all required beans"() {
        expect:
            appUserRepository != null
            noteRepository != null
            passwordEncoder != null
    }

    def "should have HelloController available"() {
        when:
            String response = helloController.hello()

        then:
            response == "Hello from my VPS!"
    }

    def "should have JwtService available"() {
        when:
            String token = jwtService.generateToken("testuser")
            String username = jwtService.extractUsername(token)

        then:
            token != null
            username == "testuser"
    }

    def "should have PasswordEncoder available"() {
        when:
            String encoded = passwordEncoder.encode("password")

        then:
            encoded != null
            passwordEncoder.matches("password", encoded)
    }

    def "should have AppUserRepository available"() {
        expect:
            appUserRepository != null
            appUserRepository.count() >= 0
    }

    def "should have NoteRepository available"() {
        expect:
            noteRepository != null
            noteRepository.count() >= 0
    }
}

