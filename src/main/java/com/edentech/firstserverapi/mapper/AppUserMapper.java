package com.edentech.firstserverapi.mapper;

import com.edentech.firstserverapi.dto.AppUserDTO;
import com.edentech.firstserverapi.entity.AppUserEntity;

public class AppUserMapper {

    /**
     * Converts AppUserEntity to AppUserDTO (without password exposure)
     */
    public static AppUserDTO toDTO(AppUserEntity user) {
        if (user == null) {
            return null;
        }
        return new AppUserDTO(user.getId(), user.getUsername());
    }

    /**
     * Converts AppUserDTO to AppUserEntity
     */
    public static AppUserEntity toEntity(AppUserDTO dto) {
        if (dto == null) {
            return null;
        }
        return new AppUserEntity(dto.getUsername(), null); // Password should be set separately
    }
}