package com.footstep.domain.posting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryDto {
    private LocalDateTime postDate; //포스트 날짜

    private String imageUrl;    //사진 url

    private String footStepName; // 발자취 이름(?)

    private int likeCount;//좋아요 개수

    private String placeName;//발자취 장소

    private int sameDatePostingCount;//동일 날짜에 속한 발자취 게시글 개수

    private Long footStepIdx;

    private int countDate;
}
