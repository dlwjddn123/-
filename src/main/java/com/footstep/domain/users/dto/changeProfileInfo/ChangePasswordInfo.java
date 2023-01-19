package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordInfo {

    @ApiParam(value = "현재 비밀번호", required = true, example = "jungWoo1234!")
    private String currentPassword;

    @ApiParam(value = "변경할 비밀번호", required = true, example = "jungWoo4321")
    private String changedPassword;
}
