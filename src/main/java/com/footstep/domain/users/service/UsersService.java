package com.footstep.domain.users.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.s3.S3UploadUtil;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.footstep.domain.base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final S3UploadUtil s3UploadUtil;

    public void join(JoinDto joinDto) throws BaseException {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        if (!usersRepository.findByEmail(joinDto.getEmail()).isEmpty()) {
            throw new BaseException(DUPLICATED_EMAIL);
        }
        if (!usersRepository.findByNickname(joinDto.getNickname()).isEmpty()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }

        usersRepository.save(Users.ofUser(joinDto));
    }

    public MyPageInfo getMyPage() throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        return new MyPageInfo(currentUsers.getNickname(), currentUsers.getPostings().size(), currentUsers.getProfileImageUrl());
    }

    public void changePassword(ChangePasswordInfo changePasswordInfo) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        if (!passwordEncoder.matches(changePasswordInfo.getCurrentPassword(), users.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }
        users.changePassword(passwordEncoder.encode(changePasswordInfo.getChangedPassword()));
        usersRepository.save(users);
    }

    public void changeNickname(String nickname) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        if (!usersRepository.findByNickname(nickname).isEmpty()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
        users.changeNickname(nickname);
        usersRepository.save(users);
    }

    public void changeProfileImage(MultipartFile profileImage) throws BaseException, IOException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        String profileImageUrl = s3UploadUtil.upload(profileImage);
        users.changeProfileImage(profileImageUrl);
        usersRepository.save(users);
    }

    public void secession(TokenDto tokenDto) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        users.secession();
        authService.logout(tokenDto, users.getEmail());
        usersRepository.save(users);
    }
}