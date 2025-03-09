package com.moldi.allora.service.implementation;

import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public HttpCookie createAccessTokenCookie(String jwt, long jwtExpiration) {
        return ResponseCookie
                .from("accessToken", jwt)
                .maxAge(jwtExpiration)
                .httpOnly(true)
                .secure(true)
                .sameSite(Cookie.SameSite.STRICT.toString())
                .path("/")
                .build();
    }

    public HttpCookie removeXsrfCookie() {
        return ResponseCookie
                .from("XSRF-TOKEN", "")
                .maxAge(0)
                .httpOnly(false)
                .secure(true)
                .sameSite(Cookie.SameSite.STRICT.toString())
                .path("/")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie
                .from("accessToken", "")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite(Cookie.SameSite.STRICT.toString())
                .path("/")
                .build();
    }
}
