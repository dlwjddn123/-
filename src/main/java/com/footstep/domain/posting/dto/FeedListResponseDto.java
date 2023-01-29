package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedListResponseDto {

    private List<FeedListDto> feedListDto;
    @ApiModelProperty(notes = "유저 피드 리스트 갯수", example = "3")
    private Long postingCount;
}
