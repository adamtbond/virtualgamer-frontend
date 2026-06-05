package com.edentech.firstserverapi.controller;

import com.edentech.firstserverapi.dto.NoteDTO;
import com.edentech.firstserverapi.entity.NoteEntity;
import com.edentech.firstserverapi.entity.AppUserEntity;
import com.edentech.firstserverapi.mapper.NoteMapper;
import com.edentech.firstserverapi.repository.NoteRepository;
import com.edentech.firstserverapi.repository.AppUserRepository;
import com.edentech.firstserverapi.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public NoteController(NoteRepository noteRepository, AppUserRepository appUserRepository, JwtService jwtService) {
        this.noteRepository = noteRepository;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    /**
     * Get all notes for the authenticated user
     */
    @GetMapping
    public List<NoteDTO> getNotes(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return noteRepository.findByUserId(user.getId())
                .stream()
                .map(NoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new note for the authenticated user
     */
    @PostMapping
    public NoteDTO createNote(@RequestBody NoteDTO noteDTO, Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        NoteEntity note = new NoteEntity(noteDTO.getText(), user);
        NoteEntity saved = noteRepository.save(note);
        return NoteMapper.toDTO(saved);
    }

    /**
     * Get a specific note by ID (only if user owns it)
     */
    @GetMapping("/{id}")
    public NoteDTO getNoteById(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return noteRepository.findByIdAndUserId(id, user.getId())
                .map(NoteMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Note not found or does not belong to user"));
    }

    /**
     * Update a note (only if user owns it)
     */
    @PutMapping("/{id}")
    public NoteDTO updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO, Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        NoteEntity note = noteRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Note not found or does not belong to user"));
        
        note.setText(noteDTO.getText());
        NoteEntity updated = noteRepository.save(note);
        return NoteMapper.toDTO(updated);
    }

    /**
     * Delete a note (only if user owns it)
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        NoteEntity note = noteRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Note not found or does not belong to user"));
        
        noteRepository.delete(note);
    }
}
