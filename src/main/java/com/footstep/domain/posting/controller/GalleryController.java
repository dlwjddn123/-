package com.footstep.domain.posting.controller;


import com.footstep.domain.posting.dto.GalleryDto;
import com.footstep.domain.posting.service.GalleryService;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/footstep")
@RequiredArgsConstructor
public class GalleryController {
    private final UsersRepository usersRepository;
    private final GalleryService galleryService;


    @ResponseBody
    @GetMapping("/gallery")
    @ApiOperation(value = "갤러리 발자취 조회", notes = "날짜 순으로 발자취 조회")
    public ResponseEntity<List<GalleryDto>> getGallery(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users currentUsers = usersRepository.findByEmail(userDetails.getUsername()).orElseThrow(()
                -> new NullPointerException("로그인이 필요합니다."));

    }
}
