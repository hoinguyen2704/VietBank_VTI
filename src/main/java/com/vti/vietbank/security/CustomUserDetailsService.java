package com.vti.vietbank.security;

import com.vti.vietbank.entity.User;
import com.vti.vietbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomUserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getPhoneNumber())
                .password(user.getPassword())
//                .authorities(new HashSet<>(getAuthorities(user))) // cách cũ
                .authorities(sortAuthorities(getAuthorities(user))) // cách mới sắp xếp
//                .authorities(getAuthorities_My(user)) // cách mình tự làm
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

    private Set<GrantedAuthority> getAuthorities_My(User user) {
        String roleName = "CUSTOMER";
        if (user.getRole() != null) {
            roleName = user.getRole().getName();
        }

        return Set.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            } else {
                return g1.getAuthority() == null ? 1 : g1.getAuthority().compareTo(g2.getAuthority());
            }
        }
    }
}
