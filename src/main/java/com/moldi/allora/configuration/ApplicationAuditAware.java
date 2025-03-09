package com.moldi.allora.configuration;

import com.moldi.allora.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("<ANONYMOUS>");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user && user.getUsername() != null) {
            return Optional.of(user.getUsername());
        }

        return Optional.of("<ANONYMOUS>");
    }
}
