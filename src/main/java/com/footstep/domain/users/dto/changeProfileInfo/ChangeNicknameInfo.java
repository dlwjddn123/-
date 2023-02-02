package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeNicknameInfo {

    @ApiModelProperty(notes = "닉네임", example = "고양이")
    @NotBlank
    private String nickname;
}
