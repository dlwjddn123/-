package com.footstep.domain.posting.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryListResponseDto {
    private List<PostingListDto> postingListDto;
}
