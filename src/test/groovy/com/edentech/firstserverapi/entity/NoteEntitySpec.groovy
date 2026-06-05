package com.edentech.firstserverapi.entity

import spock.lang.Specification

class NoteEntitySpec extends Specification {

    def "should create NoteEntity with default constructor"() {
        when:
            NoteEntity note = new NoteEntity()

        then:
            note != null
            note.text == null
            note.id == null
    }

    def "should create NoteEntity with text parameter"() {
        given:
            String text = "Test note content"

        when:
            NoteEntity note = new NoteEntity(text)

        then:
            note.text == text
            note.id == null
    }

    def "should set and get text"() {
        given:
            NoteEntity note = new NoteEntity()

        when:
            note.setText("Important note")

        then:
            note.getText() == "Important note"
    }

    def "should set and get id"() {
        given:
            NoteEntity note = new NoteEntity("Note text")

        when:
            note.setId(5L)

        then:
            note.getId() == 5L
    }

    def "should handle null text in constructor"() {
        when:
            NoteEntity note = new NoteEntity(null)

        then:
            note.text == null
    }

    def "should allow updating text"() {
        given:
            NoteEntity note = new NoteEntity("Initial text")

        when:
            note.setText("Updated text")

        then:
            note.getText() == "Updated text"
    }

    def "should preserve id"() {
        given:
            NoteEntity note = new NoteEntity("Content")
            note.setId(10L)

        when:
            Long noteId = note.getId()

        then:
            noteId == 10L
    }

    def "should create multiple notes independently"() {
        when:
            NoteEntity note1 = new NoteEntity("Note 1")
            note1.setId(1L)
            NoteEntity note2 = new NoteEntity("Note 2")
            note2.setId(2L)

        then:
            note1.getText() == "Note 1"
            note1.getId() == 1L
            note2.getText() == "Note 2"
            note2.getId() == 2L
    }

    def "should handle empty string text"() {
        when:
            NoteEntity note = new NoteEntity("")

        then:
            note.text == ""
            note.getText() == ""
    }

    def "should handle long text content"() {
        given:
            String longText = "A" * 1000

        when:
            NoteEntity note = new NoteEntity(longText)

        then:
            note.getText() == longText
            note.getText().length() == 1000
    }

    def "should allow modifying id after creation"() {
        given:
            NoteEntity note = new NoteEntity("Text")
            note.setId(1L)

        when:
            note.setId(2L)

        then:
            note.getId() == 2L
    }
}

