package com.footstep.domain.users.service;

import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    
    public MyPageInfo getMyPage(Authentication authentication) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        Users currentUsers = usersRepository.findByEmail(details.getUsername()).orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        return new MyPageInfo(currentUsers.getNickname(), currentUsers.getPostings().size());
    }

    public void changePassword(Authentication authentication, ChangePasswordInfo changePasswordInfo) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        Users users = usersRepository.findByEmail(details.getUsername()).orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        if (!passwordEncoder.matches(changePasswordInfo.getCurrentPassword(), users.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        users.changePassword(passwordEncoder.encode(changePasswordInfo.getChangedPassword()));
        usersRepository.save(users);
    }

    public void changeNickname(Authentication authentication, String nickname) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        Users users = usersRepository.findByEmail(details.getUsername()).orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        users.changeNickname(nickname);
        usersRepository.save(users);
    }

    public void changeProfileImage(Authentication authentication, String imageUrl) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        Users users = usersRepository.findByEmail(details.getUsername()).orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        users.changeProfileImage(imageUrl);
        usersRepository.save(users);
    }

    public void secession(Authentication authentication, TokenDto tokenDto) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        Users users = usersRepository.findByEmail(details.getUsername()).orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        users.secession();
        authService.logout(tokenDto, users.getEmail());
        usersRepository.save(users);
    }
}