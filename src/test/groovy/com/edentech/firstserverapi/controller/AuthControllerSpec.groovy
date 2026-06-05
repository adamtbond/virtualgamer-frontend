package com.edentech.firstserverapi.controller

import com.edentech.firstserverapi.dto.AuthRegisterResponse
import com.edentech.firstserverapi.dto.AuthRequest
import com.edentech.firstserverapi.dto.AuthResponse
import com.edentech.firstserverapi.entity.AppUserEntity
import com.edentech.firstserverapi.repository.AppUserRepository
import com.edentech.firstserverapi.service.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class AuthControllerSpec extends Specification {

    AuthController authController
    AppUserRepository appUserRepository
    JwtService jwtService
    PasswordEncoder passwordEncoder

    def setup() {
        appUserRepository = Mock(AppUserRepository)
        jwtService = Mock(JwtService)
        passwordEncoder = Mock(PasswordEncoder)

        authController = new AuthController()
        authController.userRepository = appUserRepository
        authController.jwtService = jwtService
        authController.passwordEncoder = passwordEncoder
    }

    def "should register a new user successfully"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("newuser")
            request.setPassword("password123")

            AppUserEntity savedUser = new AppUserEntity("newuser", "encodedPassword")
            savedUser.id = 1L

            passwordEncoder.encode("password123") >> "encodedPassword"
            appUserRepository.save(_ as AppUserEntity) >> savedUser

        when:
            AuthRegisterResponse response = authController.register(request)

        then:
            response != null
            response.message == "User registered"
            response.userId == 1L

    }

    def "should encode password before saving user"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("testuser")
            request.setPassword("plainPassword")

            AppUserEntity savedUser = new AppUserEntity("testuser", "hashedPassword")
            savedUser.id = 2L

            passwordEncoder.encode("plainPassword") >> "hashedPassword"
            appUserRepository.save(_ as AppUserEntity) >> savedUser

        when:
            authController.register(request)

        then:
            1 * passwordEncoder.encode("plainPassword")
    }

    def "should login successfully with valid credentials"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("johndoe")
            request.setPassword("password123")

            AppUserEntity user = new AppUserEntity("johndoe", "hashedPassword")
            user.id = 1L

            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            passwordEncoder.matches("password123", "hashedPassword") >> true
            jwtService.generateToken("johndoe") >> "valid.jwt.token"

        when:
            AuthResponse response = authController.login(request)

        then:
            response != null
            response.token == "valid.jwt.token"

    }

    def "should throw exception when user not found during login"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("nonexistent")
            request.setPassword("password")

            appUserRepository.findByUsername("nonexistent") >> Optional.empty()

        when:
            authController.login(request)

        then:
            RuntimeException ex = thrown()
            ex.message == "Invalid username or password"
    }

    def "should throw exception when password is incorrect"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("johndoe")
            request.setPassword("wrongpassword")

            AppUserEntity user = new AppUserEntity("johndoe", "correctHashedPassword")
            user.id = 1L

            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            passwordEncoder.matches("wrongpassword", "correctHashedPassword") >> false

        when:
            authController.login(request)

        then:
            RuntimeException ex = thrown()
            ex.message == "Invalid username or password"
    }

    def "should generate JWT token after successful login"() {
        given:
            AuthRequest request = new AuthRequest()
            request.setUsername("alice")
            request.setPassword("alicepass")

            AppUserEntity user = new AppUserEntity("alice", "hashedpass")
            user.id = 3L

            appUserRepository.findByUsername("alice") >> Optional.of(user)
            passwordEncoder.matches("alicepass", "hashedpass") >> true
            jwtService.generateToken("alice") >> "new.jwt.token"

        when:
            AuthResponse response = authController.login(request)

        then:
            response.token == "new.jwt.token"
    }
}

