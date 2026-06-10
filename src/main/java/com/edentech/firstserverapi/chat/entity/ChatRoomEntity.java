package com.edentech.firstserverapi.chat.entity;

import com.edentech.firstserverapi.entity.AppUserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    @ManyToOne(optional = false)
    private AppUserEntity createdBy;

    @Column(nullable = false)
    private Instant createdAt;
}