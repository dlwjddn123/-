package com.footstep.domain.users.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponseStatus;
import com.footstep.domain.base.Status;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.domain.auth.LogoutAccessToken;
import com.footstep.domain.users.domain.auth.RefreshToken;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.LoginDto;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.domain.users.repository.auth.LogoutAccessTokenRedisRepository;
import com.footstep.domain.users.repository.auth.RefreshTokenRedisRepository;
import com.footstep.global.config.jwt.JwtTokenUtil;
import com.footstep.global.config.jwt.constants.JwtExpiration;
import com.footstep.global.config.redis.CacheKey;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.footstep.domain.base.BaseResponseStatus.*;
import static com.footstep.global.config.jwt.constants.JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public TokenDto login(LoginDto loginDto) throws BaseException {
        Users users = usersRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new BaseException(NOT_FOUND_USERS_ID));
        if (users.getStatus().equals(Status.EXPIRED)) {
            throw new BaseException(BaseResponseStatus.EXPIRED_USERS);
        }
        if (users.getBannedDate() != null && LocalDateTime.now().isBefore(users.getBannedDate()))
            throw new BaseException(BANNED_USERS);
        checkPassword(loginDto.getPassword(), users.getPassword());
        String username = users.getEmail();
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        RefreshToken refreshToken = saveRefreshToken(username);
        return TokenDto.of(accessToken, refreshToken.getRefreshToken());
    }

    private void checkPassword(String rawPassword, String findMemberPassword) throws BaseException {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new BaseException(INVALID_PASSWORD);
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }


    @CacheEvict(value = CacheKey.USER, key = "#username")
    public void logout(TokenDto tokenDto, String username) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(() -> new BaseException(UNAUTHORIZED));
        if (!users.getEmail().equals(username)) {
            throw new BaseException(BAD_REQUEST);
        }
        String accessToken = resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        removeRefreshTokenByUser(username);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
    }

    public void removeRefreshTokenByUser(String username) { refreshTokenRedisRepository.deleteById(username); }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    public TokenDto reissue(String refreshToken) throws BaseException {
        refreshToken = resolveToken(refreshToken);
        String username = getCurrentUsername();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(() -> new BaseException(INVALID_JWT));

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueRefreshToken(refreshToken, username);
        }
        throw new BaseException(INVALID_REFRESH_TOKEN);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private TokenDto reissueRefreshToken(String refreshToken, String username) {
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String accessToken = jwtTokenUtil.generateAccessToken(username);
            return TokenDto.of(accessToken, saveRefreshToken(username).getRefreshToken());
        }
        return TokenDto.of(jwtTokenUtil.generateAccessToken(username), refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpiration.REISSUE_EXPIRATION_TIME.getValue();
    }

    public void isValid(String field) throws BaseException{
        switch (field) {
            case "email" -> throw new BaseException(POST_USERS_INVALID_EMAIL);
            case "password" -> throw new BaseException(POST_USERS_EMPTY_PASSWORD);
        }
    }
}
