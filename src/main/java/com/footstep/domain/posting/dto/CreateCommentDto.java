package com.footstep.domain.posting.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDto {

    @ApiModelProperty(name = "내용", example = "귀여워요.")
    @NotBlank
    private String content;
}
