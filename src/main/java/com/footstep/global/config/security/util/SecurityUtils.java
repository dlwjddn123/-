package com.footstep.global.config.security.util;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class SecurityUtils {

    private final UsersRepository usersRepository;

    public static String getLoggedUserEmail() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        return principal.getName();
    }
}
