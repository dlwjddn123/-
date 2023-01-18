package com.footstep.domain.posting.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String nickname;
    private String content;
    private Long commentId;

}
