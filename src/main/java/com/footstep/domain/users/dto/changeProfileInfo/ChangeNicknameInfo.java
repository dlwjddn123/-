package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeNicknameInfo {

    @ApiParam(value = "변경할 닉네임", required = true, example = "닉변")
    private String nickname;
}
