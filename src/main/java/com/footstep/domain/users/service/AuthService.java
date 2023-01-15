package com.footstep.domain.users.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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

    public void join(JoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        usersRepository.save(Users.ofUser(joinDto));
    }

    public TokenDto login(LoginDto loginDto) {
        Users users = usersRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
        if (users.getStatus().equals(Status.EXPIRED)) {
            throw new IllegalArgumentException("탈퇴한 회원입니다.");
        }
        checkPassword(loginDto.getPassword(), users.getPassword());
        String username = users.getEmail();
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        RefreshToken refreshToken = saveRefreshToken(username);
        return TokenDto.of(accessToken, refreshToken.getRefreshToken());
    }

    private void checkPassword(String rawPassword, String findMemberPassword) {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }


    @CacheEvict(value = CacheKey.USER, key = "#username")
    public void logout(TokenDto tokenDto, String username) {
        String accessToken = resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(username);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    public TokenDto reissue(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        String username = getCurrentUsername();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueRefreshToken(refreshToken, username);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
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
}
