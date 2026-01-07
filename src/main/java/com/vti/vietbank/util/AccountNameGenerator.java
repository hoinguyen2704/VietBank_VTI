package com.vti.vietbank.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class AccountNameGenerator {
    
    /**
     * Tạo tên tài khoản mặc định từ tên khách hàng
     * - Loại bỏ dấu tiếng Việt
     * - Chuyển thành chữ hoa
     * - Loại bỏ khoảng trắng thừa
     * 
     * @param customerName Tên khách hàng
     * @return Tên tài khoản mặc định
     */
    public static String generateDefaultAccountName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return "TAI KHOAN MAC DINH";
        }
        
        // Loại bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(customerName.trim(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutAccents = pattern.matcher(normalized).replaceAll("");
        
        // Chuyển thành chữ hoa và loại bỏ khoảng trắng thừa
        String result = withoutAccents.toUpperCase().replaceAll("\\s+", " ").trim();
        
        return result.isEmpty() ? "TAI KHOAN MAC DINH" : result;
    }
}
