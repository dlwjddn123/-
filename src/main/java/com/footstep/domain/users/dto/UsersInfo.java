package com.footstep.domain.users.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersInfo {

    private String email;
    private String username;
}