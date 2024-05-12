package com.example.rework.auth.jwt;

import com.example.rework.auth.MemberRole;
import com.example.rework.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class MemberDetails implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("MemberDetails.getAuthorities");
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Assuming getMemberRole() returns a String representing the role
        MemberRole memberRole = member.getRole();

        // Add the actual role as a GrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(memberRole.toString());
        authorities.add(authority);

        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }


    @Override
    public String getUsername() {
        return member.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.isState();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO
        return true;
    }
}
