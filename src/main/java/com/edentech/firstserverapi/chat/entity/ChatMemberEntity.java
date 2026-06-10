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
public class ChatMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ChatRoomEntity chatRoom;

    @ManyToOne(optional = false)
    private AppUserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMemberStatus status;

    @Column(nullable = false)
    private Instant joinedAt;

    private Instant leftAt;
}