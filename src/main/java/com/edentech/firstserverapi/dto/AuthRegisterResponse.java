// Create an AuthRegisterResponse.java
package com.edentech.firstserverapi.dto;

public class AuthRegisterResponse {
    private String message;
    private Long userId;

    public AuthRegisterResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }

    // Getters...
}