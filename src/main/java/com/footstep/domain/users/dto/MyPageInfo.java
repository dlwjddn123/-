package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageInfo {

    @ApiModelProperty(example = "newjeans")
    private String nickname;

    @ApiModelProperty(example = "5")
    private int postingCount;
}
