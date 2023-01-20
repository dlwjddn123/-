package com.footstep.domain.posting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPosting {

    @ApiModelProperty(notes = "발자취 게시일", example = "2022-08-10")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date postingDate;  //1. 포스트 날짜
    @ApiModelProperty(notes = "발자취 제목", example = "제목예시")
    private String postingName;//2. 발자취 제목
    @ApiModelProperty(notes = "발자취 내용", example = "내용예시")
    private String content; //3. 발자취 설명
    @ApiModelProperty(notes = "발자취 사진", example = "http://news.samsungdisplay.com/wp-content/uploads/2018/08/1.png")
    private String imageUrl; //4. 발자취 사진
    @ApiModelProperty(notes = "장소명", example = "서울역")
    private String placeName;   //5. 발자취 장소(위치)
    @ApiModelProperty(notes = "발자취 좋아요 수", example = "10")
    private String likeNum; //6. 발자취 좋아요 갯수
    @ApiModelProperty(notes = "발자취 작성자 닉네임", example = "위즈")
    private String nickName;    //7. 발자취 작성 유저 닉네임,
    @ApiModelProperty(notes = "발자취 댓글 리스트")
    private List<CommentDto> commentList; //8. 댓글 리스트
    @ApiModelProperty(notes = "발자취 댓글 수", example = "3")
    private String commentNum;  //9. 해당 발자취의 댓글 수
}
