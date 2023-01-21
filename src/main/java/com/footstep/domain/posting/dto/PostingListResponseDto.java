
package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostingListResponseDto {

    private List<PostingListDto> postingListDto;
    @ApiModelProperty(notes = "게시글 날짜별 카테고리 개수", example = "3")
    private Long uploadDates;
}