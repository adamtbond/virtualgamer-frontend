// NoteDTO.java
package com.edentech.firstserverapi.dto;

public class NoteDTO {
    private Long id;
    private String text;
    private Long userId;
    private String username;

    public NoteDTO() {}

    public NoteDTO(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public NoteDTO(Long id, String text, Long userId, String username) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.username = username;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
