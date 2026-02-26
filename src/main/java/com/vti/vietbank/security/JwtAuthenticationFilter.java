package com.vti.vietbank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            
            // Kiểm tra token có trong blacklist không
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                logger.warn("Token is blacklisted");
                filterChain.doFilter(request, response);
                return;
            }
            
            final String phoneNumber = jwtTokenProvider.getUsernameFromToken(jwt);

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

                if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                    userDetails.eraseCredentials(); // Xóa mật khẩu khỏi object để tránh lưu mật khẩu trong bộ nhớ
                    // Lưu thông tin người dùng vào SecurityContext
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Log authentication details
                    logger.info("Setting authentication for user: " + phoneNumber);
                    logger.info("Authorities: " + userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.error("Token validation failed for user: " + phoneNumber);
                    SecurityContextHolder.clearContext();
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: ", e);
        }

        filterChain.doFilter(request, response);
    }
}

