package com.edentech.firstserverapi.repository;

import com.edentech.firstserverapi.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}