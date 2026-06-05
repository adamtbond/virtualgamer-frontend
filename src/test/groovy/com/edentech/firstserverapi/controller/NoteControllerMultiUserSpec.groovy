package com.edentech.firstserverapi.controller

import com.edentech.firstserverapi.dto.NoteDTO
import com.edentech.firstserverapi.entity.NoteEntity
import com.edentech.firstserverapi.entity.AppUserEntity
import com.edentech.firstserverapi.mapper.NoteMapper
import com.edentech.firstserverapi.repository.NoteRepository
import com.edentech.firstserverapi.repository.AppUserRepository
import com.edentech.firstserverapi.service.JwtService
import org.springframework.security.core.Authentication
import spock.lang.Specification

class NoteControllerMultiUserSpec extends Specification {

    NoteController noteController
    NoteRepository noteRepository
    AppUserRepository appUserRepository
    JwtService jwtService
    Authentication authentication

    def setup() {
        noteRepository = Mock(NoteRepository)
        appUserRepository = Mock(AppUserRepository)
        jwtService = Mock(JwtService)
        authentication = Mock(Authentication)

        noteController = new NoteController(noteRepository, appUserRepository, jwtService)
    }

    def "should return only notes belonging to authenticated user"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            NoteEntity note1 = new NoteEntity("My note 1", user)
            note1.setId(1L)

            NoteEntity note2 = new NoteEntity("My note 2", user)
            note2.setId(2L)

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByUserId(1L) >> [note1, note2]

        when:
            List<NoteDTO> notes = noteController.getNotes(authentication)

        then:
            notes.size() == 2
            notes[0].text == "My note 1"
            notes[0].userId == 1L
            notes[0].username == "johndoe"
            notes[1].text == "My note 2"
            notes[1].userId == 1L

    }

    def "should return empty list when user has no notes"() {
        given:
            AppUserEntity user = new AppUserEntity("alice", "password")
            user.id = 2L

            authentication.getPrincipal() >> "alice"
            appUserRepository.findByUsername("alice") >> Optional.of(user)
            noteRepository.findByUserId(2L) >> []

        when:
            List<NoteDTO> notes = noteController.getNotes(authentication)

        then:
            notes.isEmpty()
    }

    def "should create note associated with authenticated user"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            NoteDTO inputDTO = new NoteDTO(null, "New note text")

            NoteEntity savedNote = new NoteEntity("New note text", user)
            savedNote.setId(5L)

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.save(_ as NoteEntity) >> savedNote

        when:
            NoteDTO result = noteController.createNote(inputDTO, authentication)

        then:
            result.text == "New note text"
            result.id == 5L
            result.userId == 1L
            result.username == "johndoe"

    }

    def "should get note only if user owns it"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            NoteEntity note = new NoteEntity("My note", user)
            note.setId(5L)

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByIdAndUserId(5L, 1L) >> Optional.of(note)

        when:
            NoteDTO result = noteController.getNoteById(5L, authentication)

        then:
            result.text == "My note"
            result.id == 5L
            result.userId == 1L

    }

    def "should throw exception when user tries to access note they don't own"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByIdAndUserId(99L, 1L) >> Optional.empty()

        when:
            noteController.getNoteById(99L, authentication)

        then:
            RuntimeException ex = thrown()
            ex.message.contains("Note not found") || ex.message.contains("does not belong to user")
    }

    def "should update note only if user owns it"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            NoteDTO updateDTO = new NoteDTO(null, "Updated text")

            NoteEntity existingNote = new NoteEntity("Old text", user)
            existingNote.setId(5L)

            NoteEntity updatedNote = new NoteEntity("Updated text", user)
            updatedNote.setId(5L)

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByIdAndUserId(5L, 1L) >> Optional.of(existingNote)
            noteRepository.save(_ as NoteEntity) >> updatedNote

        when:
            NoteDTO result = noteController.updateNote(5L, updateDTO, authentication)

        then:
            result.text == "Updated text"
            result.id == 5L

    }

    def "should throw exception when user tries to update note they don't own"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            NoteDTO updateDTO = new NoteDTO(null, "Updated text")

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByIdAndUserId(99L, 1L) >> Optional.empty()

        when:
            noteController.updateNote(99L, updateDTO, authentication)

        then:
            RuntimeException ex = thrown()
            ex.message.contains("Note not found") || ex.message.contains("does not belong to user")
    }

    def "should delete note only if user owns it"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            NoteEntity note = new NoteEntity("Note to delete", user)
            note.setId(5L)

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByIdAndUserId(5L, 1L) >> Optional.of(note)

        when:
            noteController.deleteNote(5L, authentication)

        then:
            1 * noteRepository.delete(note)
    }

    def "should throw exception when user tries to delete note they don't own"() {
        given:
            AppUserEntity user = new AppUserEntity("johndoe", "password")
            user.id = 1L

            authentication.getPrincipal() >> "johndoe"
            appUserRepository.findByUsername("johndoe") >> Optional.of(user)
            noteRepository.findByIdAndUserId(99L, 1L) >> Optional.empty()

        when:
            noteController.deleteNote(99L, authentication)

        then:
            RuntimeException ex = thrown()
            ex.message.contains("Note not found") || ex.message.contains("does not belong to user")
    }

    def "should throw exception if authenticated user not found in database"() {
        given:
            authentication.getPrincipal() >> "nonexistent"
            appUserRepository.findByUsername("nonexistent") >> Optional.empty()

        when:
            noteController.getNotes(authentication)

        then:
            RuntimeException ex = thrown()
            ex.message == "User not found"
    }

    def "different users should only see their own notes"() {
        given:
            AppUserEntity user1 = new AppUserEntity("user1", "password")
            user1.id = 1L

            AppUserEntity user2 = new AppUserEntity("user2", "password")
            user2.id = 2L

            NoteEntity user1Note = new NoteEntity("User 1's note", user1)
            user1Note.setId(1L)

            NoteEntity user2Note = new NoteEntity("User 2's note", user2)
            user2Note.setId(2L)

            authentication.getPrincipal() >> "user1"
            appUserRepository.findByUsername("user1") >> Optional.of(user1)
            noteRepository.findByUserId(1L) >> [user1Note]

        when:
            List<NoteDTO> notes = noteController.getNotes(authentication)

        then:
            notes.size() == 1
            notes[0].username == "user1"
            notes[0].text == "User 1's note"

        and:
            !notes.any { it.text == "User 2's note" }
    }
}

