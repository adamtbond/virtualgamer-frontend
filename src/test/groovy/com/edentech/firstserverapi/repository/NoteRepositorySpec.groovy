package com.edentech.firstserverapi.repository

import com.edentech.firstserverapi.entity.AppUserEntity
import com.edentech.firstserverapi.entity.NoteEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles("test")
class NoteRepositorySpec extends Specification {

    @Autowired
    NoteRepository noteRepository

    @Autowired
    AppUserRepository appUserRepository

    AppUserEntity user

    def setup() {
        user = appUserRepository.save(new AppUserEntity("testuser", "password"))
    }

    def "should save and retrieve note by id"() {
        given:
            NoteEntity note = new NoteEntity("Test note content", user)

        when:
            NoteEntity saved = noteRepository.save(note)
            Optional<NoteEntity> found = noteRepository.findById(saved.id)

        then:
            found.isPresent()
            found.get().text == "Test note content"
    }

    def "should return empty optional when note not found"() {
        when:
            Optional<NoteEntity> found = noteRepository.findById(999L)

        then:
            !found.isPresent()
            found.isEmpty()
    }

    def "should retrieve all notes"() {
        given:
            NoteEntity note1 = new NoteEntity("Note 1", user)
            NoteEntity note2 = new NoteEntity("Note 2", user)
            NoteEntity note3 = new NoteEntity("Note 3", user)

        when:
            noteRepository.save(note1)
            noteRepository.save(note2)
            noteRepository.save(note3)
            List<NoteEntity> all = noteRepository.findAll()

        then:
            all.size() == 3
    }

    def "should return empty list when no notes exist"() {
        when:
            List<NoteEntity> all = noteRepository.findAll()

        then:
            all.isEmpty()
    }

    def "should delete note"() {
        given:
            NoteEntity note = new NoteEntity("To be deleted", user)
            NoteEntity saved = noteRepository.save(note)

        when:
            noteRepository.delete(saved)
            Optional<NoteEntity> found = noteRepository.findById(saved.id)

        then:
            !found.isPresent()
    }

    def "should update note text"() {
        given:
            NoteEntity note = new NoteEntity("Original text", user)
            NoteEntity saved = noteRepository.save(note)

        when:
            saved.setText("Updated text")
            noteRepository.save(saved)
            Optional<NoteEntity> updated = noteRepository.findById(saved.id)

        then:
            updated.get().text == "Updated text"
    }

    def "should count notes"() {
        given:
            noteRepository.save(new NoteEntity("Note 1", user))
            noteRepository.save(new NoteEntity("Note 2", user))
            noteRepository.save(new NoteEntity("Note 3", user))

        when:
            long count = noteRepository.count()

        then:
            count == 3
    }

    def "should delete by id"() {
        given:
            NoteEntity note = new NoteEntity("Delete me", user)
            NoteEntity saved = noteRepository.save(note)

        when:
            noteRepository.deleteById(saved.id)
            Optional<NoteEntity> found = noteRepository.findById(saved.id)

        then:
            !found.isPresent()
    }

    def "should handle null note text"() {
        given:
            NoteEntity note = new NoteEntity(null, user)

        when:
            NoteEntity saved = noteRepository.save(note)
            Optional<NoteEntity> found = noteRepository.findById(saved.id)

        then:
            found.isPresent()
            found.get().text == null
    }

    def "should save multiple notes independently"() {
        given:
            NoteEntity note1 = new NoteEntity("Content 1", user)
            NoteEntity note2 = new NoteEntity("Content 2", user)

        when:
            NoteEntity saved1 = noteRepository.save(note1)
            NoteEntity saved2 = noteRepository.save(note2)

        then:
            saved1.id != saved2.id
            saved1.text == "Content 1"
            saved2.text == "Content 2"
    }
}

