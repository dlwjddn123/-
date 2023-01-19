package com.footstep.domain.users.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponseStatus;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.footstep.domain.base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public void join(JoinDto joinDto) throws BaseException {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        if (!usersRepository.findByEmail(joinDto.getEmail()).isEmpty()) {
            throw new BaseException(DUPLICATED_EMAIL);
        }
        usersRepository.save(Users.ofUser(joinDto));
    }

    public MyPageInfo getMyPage() throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        return new MyPageInfo(currentUsers.getNickname(), currentUsers.getPostings().size());
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
        users.changeNickname(nickname);
        usersRepository.save(users);
    }

    public void changeProfileImage(String imageUrl) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        users.changeProfileImage(imageUrl);
        usersRepository.save(users);
    }

    public void secession(TokenDto tokenDto) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        users.secession();
        authService.logout(tokenDto, users.getEmail());
        usersRepository.save(users);
    }
}