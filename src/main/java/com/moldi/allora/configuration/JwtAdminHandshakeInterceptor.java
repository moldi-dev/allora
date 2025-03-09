package com.moldi.allora.configuration;

import com.moldi.allora.enumeration.Role;
import com.moldi.allora.service.implementation.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAdminHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        String token = extractTokenFromCookies(request);

        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String username = jwtService.extractUsername(token);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (username != null && (authentication == null || authentication instanceof AnonymousAuthenticationToken || authentication instanceof UsernamePasswordAuthenticationToken)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails) && userDetails.isAccountNonLocked() && userDetails.isEnabled()) {
                boolean isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals(Role.ROLE_ADMINISTRATOR.name()));

                if (isAdmin) {
                    return true;
                }
            }
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, Exception exception) {

    }

    private String extractTokenFromCookies(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String cookieHeader = headers.getFirst(HttpHeaders.COOKIE);

        if (cookieHeader != null) {
            for (String cookie : cookieHeader.split(";")) {
                String[] cookieParts = cookie.split("=");

                if (cookieParts.length == 2 && "accessToken".equals(cookieParts[0].trim())) {
                    return cookieParts[1].trim();
                }
            }
        }

        return null;
    }
}
