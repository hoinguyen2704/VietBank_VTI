package com.vti.vietbank.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    CustomUserDetails customUserDetails;
    @BeforeEach
    void setUp() {
        customUserDetails = new CustomUserDetails(1L, "testUser", "password",
                Set.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"), new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testToString() {
        System.out.println(customUserDetails);
        System.out.println(customUserDetails.getAuthorities().toString());
    }
}