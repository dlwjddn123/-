package com.footstep.domain.posting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostingListDto {

    @ApiModelProperty(notes = "장소명", example = "서울역")
    private String placeName;
    @ApiModelProperty(notes = "발자취 게시일", example = "2022-08-10")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;
    @ApiModelProperty(notes = "발자취 사진", example = "http://news.samsungdisplay.com/wp-content/uploads/2018/08/1.png")
    private String imageUrl;
    @ApiModelProperty(notes = "발자취 제목", example = "제목예시")
    private String title;
    @ApiModelProperty(notes = "발자취 좋아요 수", example = "10")
    private Long likes;
    @ApiModelProperty(notes = "동일 날짜에 게시된 발자취 게시글 수", example = "3")
    private Long postings;
    @ApiModelProperty(notes = "발자취 게시글 인덱스", example = "1")
    private Long postingId;
}
