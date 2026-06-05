package com.edentech.firstserverapi.repository

import com.edentech.firstserverapi.entity.NoteEntity
import com.edentech.firstserverapi.entity.AppUserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles("test")
class NoteRepositoryMultiUserSpec extends Specification {

    @Autowired
    NoteRepository noteRepository

    @Autowired
    AppUserRepository appUserRepository

    def "should find notes by user ID"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            AppUserEntity savedUser = appUserRepository.save(user)

            NoteEntity note1 = new NoteEntity("Note 1", savedUser)
            NoteEntity note2 = new NoteEntity("Note 2", savedUser)

        when:
            noteRepository.save(note1)
            noteRepository.save(note2)
            List<NoteEntity> userNotes = noteRepository.findByUserId(savedUser.id)

        then:
            userNotes.size() == 2
            userNotes.every { it.user.id == savedUser.id }
    }

    def "should return empty list when user has no notes"() {
        given:
            AppUserEntity user = new AppUserEntity("alice", "password")
            AppUserEntity savedUser = appUserRepository.save(user)

        when:
            List<NoteEntity> userNotes = noteRepository.findByUserId(savedUser.id)

        then:
            userNotes.isEmpty()
    }

    def "should find note by ID and user ID when user owns it"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            AppUserEntity savedUser = appUserRepository.save(user)

            NoteEntity note = new NoteEntity("Test note", savedUser)
            NoteEntity savedNote = noteRepository.save(note)

        when:
            Optional<NoteEntity> found = noteRepository.findByIdAndUserId(savedNote.id, savedUser.id)

        then:
            found.isPresent()
            found.get().text == "Test note"
            found.get().user.id == savedUser.id
    }

    def "should return empty when note doesn't belong to user"() {
        given:
            AppUserEntity user1 = new AppUserEntity("user1", "password")
            AppUserEntity user2 = new AppUserEntity("user2", "password")

            AppUserEntity savedUser1 = appUserRepository.save(user1)
            AppUserEntity savedUser2 = appUserRepository.save(user2)

            NoteEntity note = new NoteEntity("User 1's note", savedUser1)
            NoteEntity savedNote = noteRepository.save(note)

        when:
            Optional<NoteEntity> found = noteRepository.findByIdAndUserId(savedNote.id, savedUser2.id)

        then:
            !found.isPresent()
    }

    def "should isolate notes between different users"() {
        given:
            AppUserEntity user1 = new AppUserEntity("user1", "password")
            AppUserEntity user2 = new AppUserEntity("user2", "password")

            AppUserEntity savedUser1 = appUserRepository.save(user1)
            AppUserEntity savedUser2 = appUserRepository.save(user2)

            NoteEntity user1Note = new NoteEntity("User 1's note", savedUser1)
            NoteEntity user2Note = new NoteEntity("User 2's note", savedUser2)

            noteRepository.save(user1Note)
            noteRepository.save(user2Note)

        when:
            List<NoteEntity> user1Notes = noteRepository.findByUserId(savedUser1.id)
            List<NoteEntity> user2Notes = noteRepository.findByUserId(savedUser2.id)

        then:
            user1Notes.size() == 1
            user1Notes[0].text == "User 1's note"

            user2Notes.size() == 1
            user2Notes[0].text == "User 2's note"

            user1Notes[0].user.username == "user1"
            user2Notes[0].user.username == "user2"
    }

    def "should be able to update note while maintaining user association"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            AppUserEntity savedUser = appUserRepository.save(user)

            NoteEntity note = new NoteEntity("Original text", savedUser)
            NoteEntity savedNote = noteRepository.save(note)

        when:
            savedNote.setText("Updated text")
            noteRepository.save(savedNote)

            Optional<NoteEntity> updated = noteRepository.findByIdAndUserId(savedNote.id, savedUser.id)

        then:
            updated.isPresent()
            updated.get().text == "Updated text"
            updated.get().user.id == savedUser.id
    }

    def "should be able to delete user's note"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            AppUserEntity savedUser = appUserRepository.save(user)

            NoteEntity note = new NoteEntity("To delete", savedUser)
            NoteEntity savedNote = noteRepository.save(note)

        when:
            noteRepository.delete(savedNote)

            Optional<NoteEntity> found = noteRepository.findByIdAndUserId(savedNote.id, savedUser.id)

        then:
            !found.isPresent()
    }

    def "multiple users can have notes with same text"() {
        given:
            AppUserEntity user1 = new AppUserEntity("user1", "password")
            AppUserEntity user2 = new AppUserEntity("user2", "password")

            AppUserEntity savedUser1 = appUserRepository.save(user1)
            AppUserEntity savedUser2 = appUserRepository.save(user2)

            NoteEntity note1 = new NoteEntity("Shared text", savedUser1)
            NoteEntity note2 = new NoteEntity("Shared text", savedUser2)

            noteRepository.save(note1)
            noteRepository.save(note2)

        when:
            List<NoteEntity> user1Notes = noteRepository.findByUserId(savedUser1.id)
            List<NoteEntity> user2Notes = noteRepository.findByUserId(savedUser2.id)

        then:
            user1Notes.size() == 1
            user2Notes.size() == 1

            user1Notes[0].text == user2Notes[0].text
            user1Notes[0].user.id != user2Notes[0].user.id
    }
}

