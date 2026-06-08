package com.edentech.firstserverapi.controller

import com.edentech.firstserverapi.dto.NoteDTO
import com.edentech.firstserverapi.entity.AppUserEntity
import com.edentech.firstserverapi.entity.NoteEntity
import com.edentech.firstserverapi.repository.AppUserRepository
import com.edentech.firstserverapi.repository.NoteRepository
import com.edentech.firstserverapi.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

class NoteControllerSpec extends Specification {

    NoteController noteController
    NoteRepository noteRepository
    AppUserRepository appUserRepository
    JwtService jwtService
    Authentication authentication
    AppUserEntity user

    def setup() {
        noteRepository = Mock(NoteRepository)
        appUserRepository = Mock(AppUserRepository)
        jwtService = Mock(JwtService)
        authentication = Mock(Authentication)

        user = new AppUserEntity("testuser", "password")
        user.id = 1L

        authentication.getPrincipal() >> "testuser"
        appUserRepository.findByUsername("testuser") >> Optional.of(user)

        noteController = new NoteController(noteRepository, appUserRepository, jwtService)
    }

    def "should return all notes as DTOs"() {
        given:
            NoteEntity note1 = new NoteEntity("First note", user)
            note1.setId(1L)

            NoteEntity note2 = new NoteEntity("Second note", user)
            note2.setId(2L)

            noteRepository.findByUserId(1L) >> [note1, note2]

        when:
            List<NoteDTO> notes = noteController.getNotes(authentication)

        then:
            notes.size() == 2
            notes[0].text == "First note"
            notes[0].id == 1L
            notes[1].text == "Second note"
            notes[1].id == 2L
    }

    def "should return empty list when no notes exist"() {
        given:
            noteRepository.findByUserId(1L) >> []

        when:
            List<NoteDTO> notes = noteController.getNotes(authentication)

        then:
            notes.isEmpty()
    }

    def "should create note and return DTO"() {
        given:
            NoteDTO inputDTO = new NoteDTO(null, "New note text")

            NoteEntity savedNote = new NoteEntity("New note text", user)
            savedNote.setId(3L)

            noteRepository.save(_ as NoteEntity) >> savedNote

        when:
            NoteDTO createdNote = noteController.createNote(inputDTO, authentication)

        then:
            createdNote != null
            createdNote.id == 3L
            createdNote.text == "New note text"
            createdNote.userId == 1L
            createdNote.username == "testuser"
    }

    def "should return note by id"() {
        given:
            NoteEntity note = new NoteEntity("Test note", user)
            note.setId(5L)

            noteRepository.findByIdAndUserId(5L, 1L) >> Optional.of(note)

        when:
            NoteDTO foundNote = noteController.getNoteById(5L, authentication)

        then:
            foundNote != null
            foundNote.id == 5L
            foundNote.text == "Test note"
    }

    def "should throw exception when note not found by id"() {
        given:
            noteRepository.findByIdAndUserId(999L, 1L) >> Optional.empty()

        when:
            noteController.getNoteById(999L, authentication)

        then:
            ResponseStatusException ex = thrown()
            ex.statusCode == HttpStatus.NOT_FOUND
            ex.reason == "Note not found or does not belong to user"
    }

    def "should update note successfully"() {
        given:
            Long noteId = 1L
            NoteDTO updateDTO = new NoteDTO(null, "Updated text")

            NoteEntity existingNote = new NoteEntity("Old text", user)
            existingNote.setId(noteId)

            NoteEntity updatedNote = new NoteEntity("Updated text", user)
            updatedNote.setId(noteId)

            noteRepository.findByIdAndUserId(noteId, 1L) >> Optional.of(existingNote)
            noteRepository.save(_ as NoteEntity) >> updatedNote

        when:
            NoteDTO result = noteController.updateNote(noteId, updateDTO, authentication)

        then:
            result != null
            result.text == "Updated text"
            result.id == noteId
    }

    def "should throw exception when updating non-existent note"() {
        given:
            Long noteId = 999L
            NoteDTO updateDTO = new NoteDTO(null, "Updated text")

            noteRepository.findByIdAndUserId(noteId, 1L) >> Optional.empty()

        when:
            noteController.updateNote(noteId, updateDTO, authentication)

        then:
            ResponseStatusException ex = thrown()
            ex.statusCode == HttpStatus.NOT_FOUND
            ex.reason == "Note not found or does not belong to user"
    }

    def "should delete note by id"() {
        given:
            Long noteId = 2L
            NoteEntity note = new NoteEntity("Delete me", user)
            note.setId(noteId)

            noteRepository.findByIdAndUserId(noteId, 1L) >> Optional.of(note)

        when:
            noteController.deleteNote(noteId, authentication)

        then:
            1 * noteRepository.delete(note)
    }

    def "should handle null note text gracefully"() {
        given:
            NoteDTO inputDTO = new NoteDTO(null, null)
            NoteEntity savedNote = new NoteEntity(null, user)
            savedNote.setId(1L)

            noteRepository.save(_ as NoteEntity) >> savedNote

        when:
            NoteDTO result = noteController.createNote(inputDTO, authentication)

        then:
            result != null
            result.id == 1L
            result.text == null
    }
}
