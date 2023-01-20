package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordInfo {

    @ApiModelProperty(notes = "현재 비밀번호", example = "jungWoo1234!")
    private String currentPassword;

    @ApiModelProperty(notes = "변경할 비밀번호", example = "jungWoo4321!")
    private String changedPassword;
}
