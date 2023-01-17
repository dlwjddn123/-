package com.footstep.domain.posting.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPlaceDto {

    private String name;
    private String imageUrl;
    private int postingCount;
}
