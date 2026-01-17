package com.vti.vietbank.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails extends User {
    private final long id;

    // Ẩn password khỏi JSON response
    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }

    public CustomUserDetails(long id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public CustomUserDetails(com.vti.vietbank.entity.User user) {
        super(user.getPhoneNumber(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
        this.id = user.getId();
    }

    public static CustomUserDetailsBuilder customBuilder() {
        return new CustomUserDetailsBuilder();
    }

    public static class CustomUserDetailsBuilder {
        private long id;
        private String username;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;

        public CustomUserDetailsBuilder id(long id) {
            this.id = id;
            return this;
        }

        public CustomUserDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CustomUserDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CustomUserDetailsBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public CustomUserDetails build() {
            return new CustomUserDetails(id, username, password, authorities);
        }
    }
}
