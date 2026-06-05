package com.edentech.firstserverapi.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

@SpringBootTest
class SecurityConfigSpec extends Specification {

    @Autowired
    PasswordEncoder passwordEncoder

    def "should encode password using BCrypt"() {
        given:
            String plainPassword = "myPassword123"

        when:
            String encodedPassword = passwordEncoder.encode(plainPassword)

        then:
            encodedPassword != plainPassword
            encodedPassword != null
            encodedPassword.startsWith('$2a$') || encodedPassword.startsWith('$2b$') || encodedPassword.startsWith('$2y$')
    }

    def "should match plain password with encoded password"() {
        given:
            String plainPassword = "testPassword"
            String encodedPassword = passwordEncoder.encode(plainPassword)

        when:
            boolean matches = passwordEncoder.matches(plainPassword, encodedPassword)

        then:
            matches == true
    }

    def "should not match wrong password with encoded password"() {
        given:
            String correctPassword = "correctPass"
            String wrongPassword = "wrongPass"
            String encodedPassword = passwordEncoder.encode(correctPassword)

        when:
            boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword)

        then:
            matches == false
    }

    def "should generate different hashes for same password"() {
        given:
            String password = "samePassword"

        when:
            String hash1 = passwordEncoder.encode(password)
            String hash2 = passwordEncoder.encode(password)

        then:
            hash1 != hash2
            passwordEncoder.matches(password, hash1)
            passwordEncoder.matches(password, hash2)
    }

    def "should handle empty password"() {
        given:
            String emptyPassword = ""

        when:
            String encoded = passwordEncoder.encode(emptyPassword)

        then:
            encoded != null
            encoded != emptyPassword
    }

    def "should be non-null"() {
        when:
            PasswordEncoder encoder = passwordEncoder

        then:
            encoder != null
    }

    def "should handle long password"() {
        given:
            String longPassword = "A" * 72

        when:
            String encoded = passwordEncoder.encode(longPassword)
            boolean matches = passwordEncoder.matches(longPassword, encoded)

        then:
            encoded != null
            matches == true
    }

    def "should handle special characters in password"() {
        given:
            String specialPassword = 'P@ssw0rd!#$%^&*()'

        when:
            String encoded = passwordEncoder.encode(specialPassword)
            boolean matches = passwordEncoder.matches(specialPassword, encoded)

        then:
            encoded != null
            matches == true
    }
}

