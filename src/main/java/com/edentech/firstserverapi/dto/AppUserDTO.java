package com.edentech.firstserverapi.dto;

public class AppUserDTO {
    private Long id;
    private String username;

    public AppUserDTO() {}

    public AppUserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
