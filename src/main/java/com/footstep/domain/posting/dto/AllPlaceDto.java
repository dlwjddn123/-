package com.footstep.domain.posting.dto;

import com.footstep.domain.posting.domain.place.Place;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllPlaceDto {

    List<Place> place;
}
