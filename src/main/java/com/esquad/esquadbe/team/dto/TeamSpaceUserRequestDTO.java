package com.esquad.esquadbe.team.dto;

import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.dto.UserRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record TeamSpaceUserRequestDTO (
        @NotNull
        Long id,
        // @NotBlank TeamSpaceRequestDTO teamSpace,
        @NotBlank UserRequestDTO member,
        // @NotBlank LocalDateTime createdAt,
        // @NotBlank LocalDateTime modifiedAt,
        @NotBlank String role
) {
    public TeamSpaceUser to() {
        return TeamSpaceUser.builder()
                .id(id)
                .member(member.to())
                .role(role)
                .build();
    }
}
