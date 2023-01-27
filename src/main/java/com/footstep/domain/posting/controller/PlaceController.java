package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.CreatePlaceDto;
import com.footstep.domain.posting.dto.PostingListResponseDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.service.PlaceService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "발자취 장소 API")
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NDY2MDA5MSwiZXhwIjoxNjc0OTYyNDkxfQ.W7MNMFI43SPbcw5pLhpbsuic0_nCDRcqHKPgEipV9ko")
})
public class PlaceController {

    private final PlaceService placeService;

    @ApiOperation(
            value = "특정 위치의 발자취 장소(팝업) 조회",
            notes = "특정 위치 ID를 이용해 현재 사용자가 해당 위치에 생성한 발자취에 대한 간략한 정보 조회",
            response = SpecificPlaceDto.class)
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3021, message = "없는 장소입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/{place_id}")
    public BaseResponse<SpecificPlaceDto> viewSpecificPlace(
            @ApiParam(value = "장소 ID", required = true, example = "2") @PathVariable("place_id") Long place_id,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewSpecificPlace(place_id));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "특정 위치의 발자취 장소(팝업) 클릭 후 게시물 리스트 조회",
            notes = "특정 위치 ID를 이용해 현재 사용자가 해당 위치에 생성한 발자취에 대해 리스트 형태로 조회",
            response = PostingListResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3021, message = "없는 장소입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/{place_id}/list")
    public BaseResponse<PostingListResponseDto> viewSpecificPlaceList(
            @ApiParam(value = "장소 ID", required = true, example = "2") @PathVariable("place_id") Long place_id,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewSpecificPlaceList(place_id));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "모든 발자취 조회",
            notes = "사용자가 생성한 모든 발자취의 위치 정보 조회",
            response = AllPlaceDto.class)
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    })
    @GetMapping("/all")
    public BaseResponse<List<AllPlaceDto>> viewAllPlace(@RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewAllPlace());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
