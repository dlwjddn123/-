package com.footstep.global.config.security.util;

<<<<<<< HEAD
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
=======
import com.footstep.domain.base.BaseException;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
>>>>>>> 215fbad4ecbe1893b6b34e779dc0ca10d7d0d205
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class SecurityUtils {

    private final UsersRepository usersRepository;

    public static String getLoggedUserEmail() {
<<<<<<< HEAD
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();
=======
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        return principal.getName();
>>>>>>> 215fbad4ecbe1893b6b34e779dc0ca10d7d0d205
    }
}
