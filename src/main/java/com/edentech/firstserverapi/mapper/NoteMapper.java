package com.edentech.firstserverapi.mapper;

import com.edentech.firstserverapi.dto.NoteDTO;
import com.edentech.firstserverapi.entity.NoteEntity;

public class NoteMapper {

    /**
     * Converts Note entity to NoteDTO (includes user information)
     */
    public static NoteDTO toDTO(NoteEntity note) {
        if (note == null) {
            return null;
        }
        Long userId = null;
        String username = null;
        
        if (note.getUser() != null) {
            userId = note.getUser().getId();
            username = note.getUser().getUsername();
        }
        
        return new NoteDTO(note.getId(), note.getText(), userId, username);
    }

    /**
     * Converts NoteDTO to Note entity (user must be set separately)
     */
    public static NoteEntity toEntity(NoteDTO dto) {
        if (dto == null) {
            return null;
        }
        NoteEntity note = new NoteEntity(dto.getText());
        if (dto.getId() != null) {
            note.setId(dto.getId());
        }
        return note;
    }
}