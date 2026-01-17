package com.vti.vietbank.security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service để quản lý token blacklist.
 * Trong production, nên sử dụng Redis để lưu trữ.
 */
@Service
public class TokenBlacklistService {

    // Sử dụng ConcurrentHashMap.newKeySet() để thread-safe
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Thêm token vào blacklist
     */
    public void blacklistToken(String token) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.add(token);
        }
    }

    /**
     * Kiểm tra token có trong blacklist không
     */
    public boolean isBlacklisted(String token) {
        return token != null && blacklistedTokens.contains(token);
    }

    /**
     * Xóa token khỏi blacklist (nếu cần)
     */
    public void removeFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }
}
