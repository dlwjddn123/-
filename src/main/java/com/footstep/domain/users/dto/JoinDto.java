package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinDto {

    @ApiModelProperty(example = "footstep@naver.com")
    private String email;

    @ApiModelProperty(example = "footstep12")
    private String password;

    @ApiModelProperty(example = "하마")
    private String nickname;
}