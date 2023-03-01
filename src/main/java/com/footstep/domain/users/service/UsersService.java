package com.footstep.domain.users.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.repository.LikeRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.posting.service.CommentService;
import com.footstep.domain.posting.service.PostingService;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.s3.S3UploadUtil;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.footstep.domain.base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final S3UploadUtil s3UploadUtil;
    private final PostingRepository postingRepository;
    private final PostingService postingService;
    private final CommentService commentService;
    private final LikeRepository likeRepository;

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
        if (Objects.equals(changePasswordInfo.getCurrentPassword(), changePasswordInfo.getChangedPassword())) {
            throw new BaseException(DUPLICATED_PASSWORD);
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
        List<Posting> postings = postingRepository.findByUsers(users);
        for (Posting posting : postings) {
            postingService.removePosting(posting.getId());
        }
        for (Comment comment : users.getComments()) {
            commentService.deleteComment(comment.getId());
        }
        for (Likes like : users.getLikes()) {
            likeRepository.delete(like);
        }
        usersRepository.save(users);
    }

    public void isValid(String field) throws BaseException{
        switch (field) {
            case "email" -> throw new BaseException(POST_USERS_INVALID_EMAIL);
            case "password" -> throw new BaseException(POST_USERS_EMPTY_PASSWORD);
            case "nickname" -> throw new BaseException(POST_USERS_EMPTY_NICKNAME);
            case "currentPassword" -> throw new BaseException(PATCH_USERS_EMPTY_CURRENT_PASSWORD);
            case "changedPassword" -> throw new BaseException(PATCH_USERS_EMPTY_CHANGED_PASSWORD);
        }
    }

    public void blocked(Users users) throws BaseException {
        List<Posting> postings = postingRepository.findByUsers(users);
        for (Posting posting : postings) {
            postingService.removePosting(posting.getId());
        }
        for (Comment comment : users.getComments()) {
            commentService.deleteComment(comment.getId());
        }
        for (Likes like : users.getLikes()) {
            likeRepository.delete(like);
        }
        authService.removeRefreshTokenByUser(users.getEmail());
        users.changeBannedDate(LocalDateTime.now().plusMinutes(5));
        usersRepository.save(users);
    }
}