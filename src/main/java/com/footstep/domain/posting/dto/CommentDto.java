package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    @ApiModelProperty(notes = "댓글 작성자 닉네임", example = "위즈")
    private String nickname;
    @ApiModelProperty(notes = "댓글 내용", example = "내용예시")
    private String content;
    @ApiModelProperty(notes = "댓글 인덱스", example = "2")
    private Long commentId;

}
