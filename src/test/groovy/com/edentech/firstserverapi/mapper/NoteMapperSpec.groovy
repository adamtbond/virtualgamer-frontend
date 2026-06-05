package com.edentech.firstserverapi.mapper

import com.edentech.firstserverapi.dto.NoteDTO
import com.edentech.firstserverapi.entity.NoteEntity
import spock.lang.Specification

class NoteMapperSpec extends Specification {

    def "should convert NoteEntity to NoteDTO"() {
        given:
            NoteEntity note = new NoteEntity("Test note")
            note.setId(1L)

        when:
            NoteDTO dto = NoteMapper.toDTO(note)

        then:
            dto != null
            dto.id == 1L
            dto.text == "Test note"
    }

    def "should convert NoteDTO to NoteEntity"() {
        given:
            NoteDTO dto = new NoteDTO(1L, "Test note")

        when:
            NoteEntity note = NoteMapper.toEntity(dto)

        then:
            note != null
            note.id == 1L
            note.text == "Test note"
    }

    def "should handle null NoteEntity in toDTO"() {
        when:
            NoteDTO dto = NoteMapper.toDTO(null)

        then:
            dto == null
    }

    def "should handle null NoteDTO in toEntity"() {
        when:
            NoteEntity note = NoteMapper.toEntity(null)

        then:
            note == null
    }

    def "should convert NoteEntity with null text to NoteDTO"() {
        given:
            NoteEntity note = new NoteEntity(null)
            note.setId(5L)

        when:
            NoteDTO dto = NoteMapper.toDTO(note)

        then:
            dto != null
            dto.id == 5L
            dto.text == null
    }

    def "should convert NoteDTO with null id to NoteEntity"() {
        given:
            NoteDTO dto = new NoteDTO(null, "Some text")

        when:
            NoteEntity note = NoteMapper.toEntity(dto)

        then:
            note != null
            note.text == "Some text"
            note.id == null
    }

    def "should preserve all fields during mapping"() {
        given:
            NoteEntity originalNote = new NoteEntity("Important note")
            originalNote.setId(42L)

        when:
            NoteDTO dto = NoteMapper.toDTO(originalNote)
            NoteEntity mappedNote = NoteMapper.toEntity(dto)

        then:
            mappedNote.id == originalNote.id
            mappedNote.text == originalNote.text
    }

    def "should create new instance on each conversion"() {
        given:
            NoteEntity note = new NoteEntity("Test")
            note.setId(1L)

        when:
            NoteDTO dto1 = NoteMapper.toDTO(note)
            NoteDTO dto2 = NoteMapper.toDTO(note)

        then:
            dto1 != dto2
            dto1.id == dto2.id
            dto1.text == dto2.text
    }
}

