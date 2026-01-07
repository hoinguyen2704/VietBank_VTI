package com.vti.vietbank.security;

import com.vti.vietbank.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CustomUserDetails implements UserDetails, CredentialsContainer {
    private Long id;
    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;


    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getPhoneNumber();
        this.password = user.getPassword();
        this.authorities = getAuthorities_My(user);


    }
    private Set<GrantedAuthority> getAuthorities_My(User user) {
        String roleName = "CUSTOMER";
        if (user.getRole() != null) {
            roleName = user.getRole().getName();
        }
        return Set.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }
    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
