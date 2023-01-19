
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

    List<PostingListDto> postingListDto;
    @ApiModelProperty(notes = "게시일 수", example = "3")
    Long uploadDates;
}