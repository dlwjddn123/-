package com.footstep.domain.users.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordInfo {

    private String currentPassword;
    private String changedPassword;
}
