package com.vti.vietbank2.security;

import com.vti.vietbank2.entity.User;
import com.vti.vietbank2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhoneNumber())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // Get role from user and handle null case
        String roleName = "CUSTOMER";
        if (user.getRole() != null) {
            roleName = user.getRole().getName();
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
    }
}
