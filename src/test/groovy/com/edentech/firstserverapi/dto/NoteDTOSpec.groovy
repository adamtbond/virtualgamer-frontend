package com.edentech.firstserverapi.dto

import spock.lang.Specification

class NoteDTOSpec extends Specification {

    def "should create NoteDTO with default constructor"() {
        when:
            NoteDTO dto = new NoteDTO()

        then:
            dto != null
            dto.id == null
            dto.text == null
    }

    def "should create NoteDTO with parameters"() {
        given:
            Long id = 1L
            String text = "Test note"

        when:
            NoteDTO dto = new NoteDTO(id, text)

        then:
            dto.id == id
            dto.text == text
    }

    def "should set and get id"() {
        given:
            NoteDTO dto = new NoteDTO()

        when:
            dto.setId(5L)

        then:
            dto.getId() == 5L
    }

    def "should set and get text"() {
        given:
            NoteDTO dto = new NoteDTO()

        when:
            dto.setText("Important note")

        then:
            dto.getText() == "Important note"
    }

    def "should handle null text"() {
        given:
            NoteDTO dto = new NoteDTO(1L, null)

        when:
            String text = dto.getText()

        then:
            text == null
    }

    def "should handle null id"() {
        given:
            NoteDTO dto = new NoteDTO(null, "Some text")

        when:
            Long id = dto.getId()

        then:
            id == null
    }

    def "should allow updating fields"() {
        given:
            NoteDTO dto = new NoteDTO(1L, "Initial")

        when:
            dto.setId(2L)
            dto.setText("Updated")

        then:
            dto.getId() == 2L
            dto.getText() == "Updated"
    }

    def "should handle empty string text"() {
        given:
            NoteDTO dto = new NoteDTO(1L, "")

        when:
            String text = dto.getText()

        then:
            text == ""
    }

    def "should create multiple DTOs independently"() {
        when:
            NoteDTO dto1 = new NoteDTO(1L, "Note 1")
            NoteDTO dto2 = new NoteDTO(2L, "Note 2")

        then:
            dto1.getId() == 1L
            dto1.getText() == "Note 1"
            dto2.getId() == 2L
            dto2.getText() == "Note 2"
    }
}

