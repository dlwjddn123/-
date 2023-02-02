package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinDto {

    @ApiModelProperty(example = "footstep@naver.com")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")
    private String email;

    @ApiModelProperty(example = "footstep12")
    @NotBlank
    private String password;

    @ApiModelProperty(example = "하마")
    @NotBlank
    private String nickname;
}