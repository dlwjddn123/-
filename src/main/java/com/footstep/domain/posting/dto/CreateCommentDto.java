package com.footstep.domain.posting.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDto {

    @ApiModelProperty(name = "내용", required = true, example = "댓글 내용입니다.")
    private String content;
}
