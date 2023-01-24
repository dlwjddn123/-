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

    @ApiModelProperty(example = "footstep@naver.com")
    private String email;

    @ApiModelProperty(example = "하마")
    private String password;

    @ApiModelProperty(example = "footstep12")
    private String nickname;
}