package com.example.collabnotes.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class CurrentUserProvider {

    public Long requireUserId() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName()
                .isBlank()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new org.springframework.security.access.AccessDeniedException("Invalid user ID format");
        }

    }
}
