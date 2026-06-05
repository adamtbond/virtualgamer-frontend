package com.edentech.firstserverapi.repository

import com.edentech.firstserverapi.entity.AppUserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles("test")
class AppUserRepositorySpec extends Specification {

    @Autowired
    AppUserRepository appUserRepository

    def "should save and retrieve user by username"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password123")

        when:
            appUserRepository.save(user)
            Optional<AppUserEntity> found = appUserRepository.findByUsername("johndoe")

        then:
            found.isPresent()
            found.get().username == "johndoe"
            found.get().password == "password123"
    }

    def "should return empty optional when user not found"() {
        when:
            Optional<AppUserEntity> found = appUserRepository.findByUsername("nonexistent")

        then:
            !found.isPresent()
            found.isEmpty()
    }

    def "should find user by username case sensitive"() {
        given:
            AppUserEntity user = new AppUserEntity("TestUser", "pass")
            appUserRepository.save(user)

        when:
            Optional<AppUserEntity> found = appUserRepository.findByUsername("TestUser")
            Optional<AppUserEntity> notFound = appUserRepository.findByUsername("testuser")

        then:
            found.isPresent()
            !notFound.isPresent()
    }

    def "should save multiple users"() {
        given:
            AppUserEntity user1 = new AppUserEntity("user1", "pass1")
            AppUserEntity user2 = new AppUserEntity("user2", "pass2")

        when:
            appUserRepository.save(user1)
            appUserRepository.save(user2)
            long count = appUserRepository.count()

        then:
            count == 2
    }

    def "should retrieve user by id"() {
        given:
            AppUserEntity user = new AppUserEntity("alice", "alicepass")
            AppUserEntity saved = appUserRepository.save(user)

        when:
            Optional<AppUserEntity> found = appUserRepository.findById(saved.id)

        then:
            found.isPresent()
            found.get().username == "alice"
    }

    def "should delete user"() {
        given:
            AppUserEntity user = new AppUserEntity("tobedeleted", "pass")
            AppUserEntity saved = appUserRepository.save(user)

        when:
            appUserRepository.delete(saved)
            Optional<AppUserEntity> found = appUserRepository.findById(saved.id)

        then:
            !found.isPresent()
    }

    def "should update user"() {
        given:
            AppUserEntity user = new AppUserEntity("original", "pass")
            AppUserEntity saved = appUserRepository.save(user)

        when:
            saved.setPassword("newpassword")
            appUserRepository.save(saved)
            Optional<AppUserEntity> updated = appUserRepository.findById(saved.id)

        then:
            updated.get().password == "newpassword"
    }
}

