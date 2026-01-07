# Hướng Dẫn Sử Dụng Swagger/OpenAPI

## Tổng Quan

Dự án đã được tích hợp **SpringDoc OpenAPI** (Swagger) để tự động tạo tài liệu API tương tác.

## Truy Cập Swagger UI

Sau khi chạy ứng dụng, truy cập:

```
http://localhost:8080/swagger-ui.html
```

Hoặc:

```
http://localhost:8080/swagger-ui/index.html
```

## Truy Cập OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

## Tính Năng

### 1. Xem Tất Cả API Endpoints
- Tất cả các controller và endpoints được tự động scan
- Phân loại theo tags (Authentication, Notifications, Accounts, etc.)

### 2. Test API Trực Tiếp
- Click vào endpoint để xem chi tiết
- Click "Try it out" để test API
- Điền request body và click "Execute"
- Xem response ngay lập tức

### 3. Authentication với JWT
1. Đăng nhập tại `/api/auth/login` để lấy token
2. Click nút **"Authorize"** ở góc trên bên phải
3. Nhập: `Bearer {your_token}` (không có dấu ngoặc nhọn)
4. Click "Authorize"
5. Bây giờ có thể test các API cần authentication

## Cấu Hình

### OpenApiConfig.java
File cấu hình chính cho Swagger:
- Title: "VietBank API Documentation"
- Version: "1.0.0"
- Security: JWT Bearer Token
- Description: Mô tả đầy đủ về hệ thống

### application.properties
```properties
# Swagger/OpenAPI configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
```

## Cách Sử Dụng

### Bước 1: Khởi động ứng dụng
```bash
mvn spring-boot:run
```

### Bước 2: Mở Swagger UI
Truy cập: `http://localhost:8080/swagger-ui.html`

### Bước 3: Đăng nhập để lấy token
1. Tìm endpoint `POST /api/auth/login`
2. Click "Try it out"
3. Điền request body:
```json
{
  "phoneNumber": "0900000001",
  "password": "123456"
}
```
4. Click "Execute"
5. Copy `accessToken` từ response

### Bước 4: Authorize với token
1. Click nút **"Authorize"** (🔒) ở góc trên
2. Nhập: `Bearer {paste_token_here}`
3. Click "Authorize"
4. Click "Close"

### Bước 5: Test các API khác
Bây giờ có thể test bất kỳ API nào cần authentication!

## Ví Dụ Test API

### Test Lấy Thông Báo
1. Tìm `GET /api/notifications`
2. Click "Try it out"
3. Click "Execute"
4. Xem response với danh sách thông báo

### Test Chuyển Tiền
1. Tìm `POST /api/transactions/transfer`
2. Click "Try it out"
3. Điền request body:
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 100000,
  "description": "Test transfer",
  "createdBy": 1
}
```
4. Click "Execute"
5. Xem response và kiểm tra thông báo real-time!

## Annotations Đã Thêm

### @Tag
Phân loại các controller:
```java
@Tag(name = "Authentication", description = "API xác thực và quản lý JWT token")
```

### @Operation
Mô tả từng endpoint:
```java
@Operation(summary = "Đăng nhập", description = "Đăng nhập bằng số điện thoại và mật khẩu để nhận JWT token")
```

### @SecurityRequirement
Yêu cầu authentication:
```java
@SecurityRequirement(name = "Bearer Authentication")
```

## Tùy Chỉnh Thêm

### Thêm Description cho DTO
```java
@Schema(description = "Request đăng nhập")
public class LoginRequest {
    @Schema(description = "Số điện thoại", example = "0900000001")
    private String phoneNumber;
    
    @Schema(description = "Mật khẩu", example = "123456")
    private String password;
}
```

### Thêm Example Values
```java
@Schema(example = "1000000")
private BigDecimal amount;
```

## Troubleshooting

### Swagger UI không load
- Kiểm tra port có đúng không (mặc định 8080)
- Kiểm tra SecurityConfig đã permit Swagger endpoints chưa
- Xem logs để kiểm tra lỗi

### Không test được API cần auth
- Đảm bảo đã click "Authorize" và nhập token đúng format
- Token phải có prefix "Bearer "
- Kiểm tra token còn hạn không

### API không hiển thị
- Kiểm tra controller có annotation `@RestController` không
- Kiểm tra package có trong scan path không
- Restart ứng dụng

## Lợi Ích

✅ **Tự động tạo tài liệu** - Không cần viết tay  
✅ **Test trực tiếp** - Không cần Postman  
✅ **Luôn cập nhật** - Tự động sync với code  
✅ **Dễ chia sẻ** - Export OpenAPI JSON  
✅ **Tương tác** - UI thân thiện  

## Export OpenAPI Spec

Để export OpenAPI specification:
```bash
curl http://localhost:8080/v3/api-docs > openapi.json
```

Có thể import vào:
- Postman
- Insomnia
- Swagger Editor
- Các công cụ khác hỗ trợ OpenAPI

## Production

⚠️ **Lưu ý**: Trong production, nên disable Swagger UI hoặc giới hạn truy cập:

```java
@Profile("!prod")
@Configuration
public class OpenApiConfig {
    // Chỉ enable trong dev/test
}
```

Hoặc trong `application-prod.properties`:
```properties
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

