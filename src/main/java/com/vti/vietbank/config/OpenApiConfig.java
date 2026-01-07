package com.vti.vietbank.config;

import com.vti.vietbank.security.CustomUserDetails;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    static {
        // Hide UserDetails and other problematic types from Swagger schema
        SpringDocUtils.getConfig().addResponseTypeToIgnore(CustomUserDetails.class);
        // Also ignore Hibernate proxies
        try {
            SpringDocUtils.getConfig().addResponseTypeToIgnore(org.hibernate.proxy.HibernateProxy.class);
        } catch (Exception e) {
            // Ignore if class not found
        }
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VietBank API Documentation")
                        .version("1.0.0")
                        .description("""
                                Hệ thống quản lý ngân hàng số Việt Bank
                                
                                ## Tính năng chính:
                                - 🔐 Authentication & Authorization với JWT
                                - 💰 Quản lý tài khoản (Account Management)
                                - 💸 Giao dịch (Deposit, Withdrawal, Transfer)
                                - 👥 Quản lý khách hàng (Customer Management)
                                - 👔 Quản lý nhân viên (Staff Management)
                                - 📊 Quản lý phòng ban và chức vụ
                                - 🔔 Thông báo real-time qua WebSocket
                                
                                ## Authentication:
                                Sử dụng JWT Bearer Token. Đăng nhập tại `/api/auth/login` để lấy token.
                                Sau đó thêm header: `Authorization: Bearer {token}`
                                """)
                        .contact(new Contact()
                                .name("VietBank Support")
                                .email("support@vietbank.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("vietbank-api")
                .pathsToMatch("/api/**")
                .pathsToExclude("/api/auth/profile") // Exclude problematic endpoint
                .packagesToScan("com.vti.vietbank.controller")
                .packagesToExclude("com.vti.vietbank.exception") // Exclude exception handlers
                .build();
    }
}
