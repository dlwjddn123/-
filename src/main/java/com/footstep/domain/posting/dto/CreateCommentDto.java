package com.footstep.domain.posting.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDto {

    @ApiModelProperty(name = "내용", required = true, example = "귀여워요.")
    private String content;
}
