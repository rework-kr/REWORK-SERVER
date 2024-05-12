package com.example.rework.auth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60; //1 hours
    public final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14; // 14 week

    private final String HEADER_NAME = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public String generateAccessToken(String username, Authentication authentication) {


        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("username", username)
                .withClaim("ROLE",roles)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }


    public String generateRefreshToken(String username, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("username", username)
                .withClaim("ROLE",roles)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public boolean verifyToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getHeader(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        if (header != null && !header.startsWith(TOKEN_PREFIX)) header = null;
        return header;
    }

    public String getUserId(HttpServletRequest request) {
        String accessToken = getHeader(request).replace(TOKEN_PREFIX, "");
        System.out.println("test=>"+accessToken);
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(accessToken)
                .getClaim("username").asString();
    }
    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("username").asString();
    }
    public List<String> getRolesFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("ROLE")
                .asList(String.class);
    }
    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        List<String> roles = getRolesFromToken(token);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(username, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Authentication validateAndSetAuthentication(String refreshToken) {
        Authentication authentication = this.getAuthentication(refreshToken);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        this.verifyToken(refreshToken);
        return authentication;
    }

    public String renewRefreshToken(Authentication authentication) {
        return this.generateRefreshToken(authentication.getName(), authentication);
    }


}