package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.PostingListResponseDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.dto.*;
import com.footstep.domain.posting.service.PlaceService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Api(tags = "발자취 장소 API")
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NTIyOTAxOSwiZXhwIjoxNjc1NTMxNDE5fQ.aXwUa5FDYUPoNbZQIZ0ktnwImbCxn2SaTnV-S6e7sj4")
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
    @GetMapping("/{place-id}")
    public BaseResponse<SpecificPlaceDto> viewSpecificPlace(
            @ApiParam(value = "장소 ID", required = true, example = "2") @PathVariable("place-id") Long placeId,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewSpecificPlace(placeId));
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
    @GetMapping("/{place-id}/list")
    public BaseResponse<PostingListResponseDto> viewSpecificPlaceList(
            @ApiParam(value = "장소 ID", required = true, example = "2") @PathVariable("place-id") Long placeId,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewSpecificPlaceList(placeId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "도시 설정 후 지도에 발자취 표시",
            notes = "특정 위치 ID를 이용해 현재 사용자가 해당 위치에 생성한 발자취에 대해 리스트 형태로 조회",
            response = AllPlaceDto.class)
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3022, message = "없는 도시입니다.")
    })
    @GetMapping("/city/{city-name}")
    public BaseResponse<List<AllPlaceDto>> viewSpecificCity(
            @ApiParam(value = "도시명", required = true, example = "서울") @PathVariable("city-name") String cityName,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewSpecificCity(cityName));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "검색 장소 지도 표시",
            notes = "특정 위치의 위도와 경도를 이용해 현재 사용자가 해당 위치에 생성한 발자취에 대한 정보를 지도에 표시",
            response = PlaceLocationDto.class)
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3021, message = "없는 장소입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/{latitude}/{longitude}")
    public BaseResponse<PlaceLocationDto> viewPlaceLocation(
            @ApiParam(value = "위도", required = true, example = "37.5776087830657") @PathVariable("latitude") Double latitude,
            @ApiParam(value = "경도", required = true, example = "126.976896737645") @PathVariable("longitude") Double longitude,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewPlaceLocation(latitude, longitude));
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

    @ApiOperation(
            value = "특정 위치의 발자취 장소(팝업) 클릭 후 지정 날짜 게시물 리스트 조회",
            notes = "특정 위치 ID를 이용해 현재 사용자가 해당 위치, 해당 날짜에 생성한 발자취에 대해 리스트 형태로 조회",
            response = DesignatedPostingDto.class)
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3021, message = "없는 장소입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/{place-id}/{date}/list")
    public BaseResponse<DesignatedPostingDto> viewSpecificPlaceDateList(
            @ApiParam(value = "장소 ID", required = true, example = "2") @PathVariable("place-id") Long placeId,
            @ApiParam(value = "선택 날짜", required = true, example = "2023-01-23") @PathVariable("date") Date date,
            @RequestHeader("Authorization")String accessToken) {
        try {
            return new BaseResponse<>(placeService.viewSpecificPlaceDateList(placeId, date));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
