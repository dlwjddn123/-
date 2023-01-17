package com.footstep.domain.users.dto.changeProfileInfo;

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
