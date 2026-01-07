package com.vti.vietbank.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHelper {

    public static Integer getCurrentUserId() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        // Note: This returns phone number as string, you may need to convert to user ID
        // For now, we'll use username (phone number) and convert in service layer
        return null; // Will be handled by getting user from repository
    }

    public static String getCurrentUserIdAsString() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // Get username (phone number) from authentication
        return authentication.getName();
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }

    public static boolean isCustomer() {
        return hasRole("CUSTOMER");
    }

    public static boolean isStaff() {
        return hasRole("STAFF");
    }

    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }
}
