package com.edentech.firstserverapi.controller

import com.edentech.firstserverapi.repository.AppUserRepository
import com.edentech.firstserverapi.repository.NoteRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class NoteHttpIntegrationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    NoteRepository noteRepository

    @Autowired
    AppUserRepository appUserRepository

    ObjectMapper objectMapper = new ObjectMapper()

    def setup() {
        noteRepository.deleteAll()
        appUserRepository.deleteAll()
    }

    def "notes are created listed and deleted only for the bearer token user"() {
        given:
            String suffix = System.nanoTime().toString()
            String aliceToken = registerAndLogin("alice_${suffix}", "password123")
            String bobToken = registerAndLogin("bob_${suffix}", "password123")

        when:
            Map aliceFirstNote = createNote(aliceToken, "Alice first note")
            createNote(aliceToken, "Alice second note")
            createNote(bobToken, "Bob private note")

        then:
            List aliceNotes = listNotes(aliceToken)
            aliceNotes*.text as Set == ["Alice first note", "Alice second note"] as Set
            aliceNotes.every { it.username == "alice_${suffix}" }

        and:
            List bobNotes = listNotes(bobToken)
            bobNotes*.text == ["Bob private note"]
            bobNotes.every { it.username == "bob_${suffix}" }

        when:
            mockMvc.perform(delete("/notes/${aliceFirstNote.id}")
                    .header("Authorization", "Bearer ${bobToken}"))
                    .andExpect(status().isNotFound())

        then:
            listNotes(aliceToken)*.text as Set == ["Alice first note", "Alice second note"] as Set
            listNotes(bobToken)*.text == ["Bob private note"]

        when:
            mockMvc.perform(delete("/notes/${aliceFirstNote.id}")
                    .header("Authorization", "Bearer ${aliceToken}"))
                    .andExpect(status().isNoContent())

        then:
            listNotes(aliceToken)*.text == ["Alice second note"]
            listNotes(bobToken)*.text == ["Bob private note"]
    }

    private String registerAndLogin(String username, String password) {
        String credentials = """{"username":"${username}","password":"${password}"}"""

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials))
                .andExpect(status().isOk())

        String response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return objectMapper.readValue(response, Map).token
    }

    private Map createNote(String token, String text) {
        String response = mockMvc.perform(post("/notes")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"text":"${text}"}"""))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return objectMapper.readValue(response, Map)
    }

    private List listNotes(String token) {
        String response = mockMvc.perform(get("/notes")
                .header("Authorization", "Bearer ${token}"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return objectMapper.readValue(response, List)
    }
}
