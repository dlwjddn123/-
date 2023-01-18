
package com.footstep.domain.posting.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostingListResponseDto {

    List<PostingListDto> specificPlaceListDto;
    Long uploadDates;
}