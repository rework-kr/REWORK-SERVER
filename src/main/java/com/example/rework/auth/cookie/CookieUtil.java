package com.example.rework.auth.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None") // None, Lax, Strict
                .httpOnly(true)
//                .secure(true) //TODO: secure true로 변경
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static String getRefreshTokenCookie(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();

        String cookieRefreshToken = "";
        for (jakarta.servlet.http.Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                cookieRefreshToken = cookie.getValue();
            }
        }
        return cookieRefreshToken;
    }
    public static void deleteRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.addCookie(response,"refreshToken", null,0);
    }

}
