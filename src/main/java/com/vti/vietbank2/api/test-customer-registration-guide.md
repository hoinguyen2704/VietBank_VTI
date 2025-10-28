# Test Customer Registration API - Đã sửa lỗi

## Vấn đề đã được sửa

### ✅ **Lỗi "ID conflict trong Customer entity"**
- **Nguyên nhân**: `customer.setId(user.getId())` gây xung đột với `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Giải pháp**: Loại bỏ việc set ID thủ công, để JPA tự động generate ID

### ✅ **CustomerServiceImpl đã được cải thiện**
1. Loại bỏ `customer.setId(user.getId())`
2. Để JPA tự động generate ID cho Customer
3. Vẫn giữ liên kết với User thông qua `user_id`

## Test API Customer Registration

### 1. Test customer registration API
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d @test-customer-registration.json
```

## Request mẫu

```json
{
  "phoneNumber": "0828443833",
  "password": "123456",
  "fullName": "Nguyen Tien hoi",
  "email": "hoi@gmail.com",
  "dateOfBirth": "27/04/2003",
  "gender": "MALE",
  "citizenId": "024203000752",
  "address": "cau dien"
}
```

## Expected Response

```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "id": 123,
    "phoneNumber": "0828443833",
    "fullName": "Nguyen Tien hoi",
    "email": "hoi@gmail.com",
    "dateOfBirth": "2003-04-27T00:00:00",
    "gender": "MALE",
    "citizenId": "024203000752",
    "address": "cau dien",
    "deleted": false,
    "createAt": "2024-01-01T10:00:00",
    "updateAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

## Lưu ý

- Đảm bảo phone number chưa được sử dụng
- Đảm bảo citizen ID chưa được sử dụng (nếu có)
- Date format phải là `dd/MM/yyyy`
- Password phải có ít nhất 6 ký tự
- Phone number phải có 10-11 chữ số
- Citizen ID phải có 9-12 chữ số
- Customer ID sẽ được tự động generate bởi JPA