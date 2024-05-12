package com.example.rework.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class SecurityUtils {

    private static SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");

    private static List<SimpleGrantedAuthority> notUserAuthority = List.of(admin);

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다");
        }

        if (authentication.isAuthenticated()
                && !CollectionUtils.containsAny(
                authentication.getAuthorities(), notUserAuthority)) {
            return Long.valueOf(authentication.getName());
        }
        // admin 유저일시 유저 아이디 0 반환
        return 0L;
    }
}