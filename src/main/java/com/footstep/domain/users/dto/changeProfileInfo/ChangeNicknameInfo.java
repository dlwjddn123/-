package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeNicknameInfo {

    @ApiModelProperty(notes = "닉네임", example = "newjeans")
    private String nickname;
}
