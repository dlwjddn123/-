package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinDto {

    @ApiModelProperty(example = "lee1234@naver.com")
    private String email;

    @ApiModelProperty(example = "leejw1234")
    private String password;

    @ApiModelProperty(example = "newJeans")
    private String nickname;
}