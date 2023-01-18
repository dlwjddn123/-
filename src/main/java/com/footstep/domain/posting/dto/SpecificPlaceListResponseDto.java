package com.footstep.domain.posting.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPlaceListResponseDto {

    List<SpecificPlaceListDto> specificPlaceListDto;
    Long uploadDates;
}
