package com.footstep.domain.base;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_REFRESH_TOKEN(false, 2004, "토큰이 일치하지 않습니다"),
    UNAUTHORIZED(false, 2005, "로그인이 필요합니다."),
    BAD_REQUEST(false, 2006, "잘못된 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(false, 3017, "이미 존재하는 닉네임입니다."),
    NOT_FOUND_USERS_ID(false,3014,"없는 아이디입니다."),
    INVALID_PASSWORD(false, 3015, "비밀번호가 다릅니다."),
    EXPIRED_USERS(false, 3016, "탈퇴한 회원입니다."),

    // Place
    NOT_FOUND_PLACE(false, 3021, "없는 장소입니다."),
    NOT_FOUND_CITY(false, 3022, "없는 도시입니다."),

    // Posting
    NOT_FOUND_POSTING(false, 3031, "게시글이 존재하지 않습니다."),

    // Comment
    NOT_FOUND_COMMENT(false, 3041, "댓글이 존재하지 않습니다."),
    // Like
    NOT_FOUND_LIKE(false, 3051, "좋아요를 찾을 수 없습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
