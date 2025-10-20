# 🏦 ACCOUNT API ENDPOINTS - VietBank Digital

## 📋 DANH SÁCH API ĐÃ HOÀN THÀNH

### **1. 📊 GET /api/accounts - Lấy danh sách tài khoản (có phân trang, filter)**

**Mô tả:** Lấy danh sách tài khoản với khả năng phân trang, sắp xếp và lọc dữ liệu

**Query Parameters:**
- `accountNumber` (optional): Tìm kiếm theo số tài khoản
- `customerId` (optional): Lọc theo ID khách hàng
- `accountTypeId` (optional): Lọc theo loại tài khoản
- `status` (optional): Lọc theo trạng thái (ACTIVE, INACTIVE, SUSPENDED, CLOSED)
- `minBalance` (optional): Số dư tối thiểu
- `maxBalance` (optional): Số dư tối đa
- `openedFrom` (optional): Ngày mở từ (format: yyyy-MM-ddTHH:mm:ss)
- `openedTo` (optional): Ngày mở đến (format: yyyy-MM-ddTHH:mm:ss)
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng mỗi trang
- `sortBy` (default: "id"): Sắp xếp theo trường
- `sortDirection` (default: "asc"): Hướng sắp xếp (asc/desc)

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "content": [...],
    "page": 0,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **2. ✏️ PUT /api/accounts/{id} - Cập nhật thông tin tài khoản**

**Mô tả:** Cập nhật trạng thái và thông tin tài khoản

**Path Parameters:**
- `id`: ID của tài khoản

**Request Body:**
```json
{
  "status": "ACTIVE",
  "closedDate": "2024-12-31T23:59:59"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Account updated successfully",
  "data": {
    "id": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "accountTypeName": "Tiết kiệm",
    "balance": 1000000.00,
    "status": "ACTIVE",
    "openedDate": "2024-01-01T10:00:00",
    "closedDate": null,
    "createAt": "2024-01-01T10:00:00",
    "updateAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **3. 🗑️ DELETE /api/accounts/{id} - Đóng tài khoản (soft delete)**

**Mô tả:** Đóng tài khoản bằng cách thay đổi trạng thái thành CLOSED

**Path Parameters:**
- `id`: ID của tài khoản

**Response:**
```json
{
  "success": true,
  "message": "Account closed successfully",
  "data": null,
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **4. 👤 GET /api/accounts/customer/{customerId} - Lấy tài khoản theo khách hàng**

**Mô tả:** Lấy danh sách tất cả tài khoản của một khách hàng

**Path Parameters:**
- `customerId`: ID của khách hàng

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "accountNumber": "1234567890",
      "customerName": "Nguyễn Văn A",
      "accountTypeName": "Tiết kiệm",
      "balance": 1000000.00,
      "status": "ACTIVE",
      "openedDate": "2024-01-01T10:00:00",
      "closedDate": null,
      "createAt": "2024-01-01T10:00:00",
      "updateAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **5. 💰 GET /api/accounts/{id}/balance - Kiểm tra số dư theo ID**

**Mô tả:** Lấy số dư tài khoản theo ID

**Path Parameters:**
- `id`: ID của tài khoản

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": 1000000.00,
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **6. 💰 GET /api/accounts/{accountNumber}/balance - Kiểm tra số dư theo số tài khoản**

**Mô tả:** Lấy số dư tài khoản theo số tài khoản

**Path Parameters:**
- `accountNumber`: Số tài khoản

**Response:**
```json
1000000.00
```

---

### **7. 📝 POST /api/accounts - Mở tài khoản mới**

**Mô tả:** Tạo tài khoản mới cho khách hàng

**Request Body:**
```json
{
  "accountNumber": "1234567890",
  "customerId": 1,
  "accountTypeId": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Account opened",
  "data": {
    "id": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "accountTypeName": "Tiết kiệm",
    "balance": 0.00,
    "status": "ACTIVE",
    "openedDate": "2024-01-01T10:00:00",
    "closedDate": null,
    "createAt": "2024-01-01T10:00:00",
    "updateAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **8. 🔍 GET /api/accounts/{accountNumber} - Lấy thông tin tài khoản theo số tài khoản**

**Mô tả:** Lấy thông tin chi tiết tài khoản theo số tài khoản

**Path Parameters:**
- `accountNumber`: Số tài khoản

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "accountTypeName": "Tiết kiệm",
    "balance": 1000000.00,
    "status": "ACTIVE",
    "openedDate": "2024-01-01T10:00:00",
    "closedDate": null,
    "createAt": "2024-01-01T10:00:00",
    "updateAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 🎯 TÍNH NĂNG NỔI BẬT

### **✅ Đã hoàn thành:**
- ✅ Phân trang và sắp xếp dữ liệu
- ✅ Lọc dữ liệu theo nhiều tiêu chí
- ✅ Cập nhật trạng thái tài khoản
- ✅ Soft delete (đóng tài khoản)
- ✅ Tìm kiếm theo khách hàng
- ✅ Kiểm tra số dư
- ✅ Validation đầu vào
- ✅ Exception handling
- ✅ Transaction management

### **🔧 Công nghệ sử dụng:**
- Spring Boot 3.5.5
- Spring Data JPA với Specification
- Lombok cho code generation
- Validation API
- DTO pattern
- RESTful API design

### **📊 DTOs đã tạo:**
- `AccountFilterRequest` - Lọc và phân trang
- `UpdateAccountRequest` - Cập nhật tài khoản
- `PageResponse<T>` - Response phân trang
- `AccountResponse` - Thông tin tài khoản
- `ApiResponse<T>` - Wrapper response

---

## 🚀 CÁCH SỬ DỤNG

### **Ví dụ lấy danh sách tài khoản với filter:**
```
GET /api/accounts?status=ACTIVE&minBalance=100000&page=0&size=5&sortBy=balance&sortDirection=desc
```

### **Ví dụ cập nhật tài khoản:**
```
PUT /api/accounts/1
Content-Type: application/json

{
  "status": "SUSPENDED",
  "closedDate": "2024-12-31T23:59:59"
}
```

### **Ví dụ đóng tài khoản:**
```
DELETE /api/accounts/1
```

---

## 📈 BƯỚC TIẾP THEO

1. **Transaction APIs** - Nạp/rút/chuyển tiền
2. **Authentication APIs** - Đăng nhập/đăng xuất
3. **Staff Management APIs** - Quản lý nhân viên
4. **Reporting APIs** - Báo cáo thống kê
