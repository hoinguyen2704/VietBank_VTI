# 👥 CUSTOMER & STAFF API ENDPOINTS - VietBank Digital

## 📋 DANH SÁCH API ĐÃ HOÀN THÀNH

---

## 👥 CUSTOMER APIs (MỞ RỘNG)

### **1. ✏️ PUT /api/customers/{id} - Cập nhật thông tin khách hàng**

**Mô tả:** Cập nhật thông tin cá nhân của khách hàng

**Path Parameters:**
- `id`: ID của khách hàng

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn A",
  "email": "nguyenvana@email.com",
  "dateOfBirth": "1990-01-01",
  "gender": "MALE",
  "citizenId": "123456789",
  "address": "123 Đường ABC, Quận 1, TP.HCM"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Customer updated successfully",
  "data": {
    "id": 1,
    "phoneNumber": "0123456789",
    "fullName": "Nguyễn Văn A",
    "email": "nguyenvana@email.com",
    "dateOfBirth": "1990-01-01",
    "gender": "MALE",
    "citizenId": "123456789",
    "address": "123 Đường ABC, Quận 1, TP.HCM",
    "isDeleted": false,
    "createAt": "2024-01-01T10:00:00",
    "updateAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **2. 🗑️ DELETE /api/customers/{id} - Xóa khách hàng (soft delete)**

**Mô tả:** Xóa khách hàng bằng cách đánh dấu isDeleted = true

**Path Parameters:**
- `id`: ID của khách hàng

**Response:**
```json
{
  "success": true,
  "message": "Customer deleted successfully",
  "data": null,
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **3. 🔍 GET /api/customers/search - Tìm kiếm khách hàng**

**Mô tả:** Tìm kiếm khách hàng theo tên, phone, CCCD, email

**Query Parameters:**
- `fullName` (optional): Tìm kiếm theo tên
- `phoneNumber` (optional): Tìm kiếm theo số điện thoại
- `citizenId` (optional): Tìm kiếm theo CCCD
- `email` (optional): Tìm kiếm theo email

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "phoneNumber": "0123456789",
      "fullName": "Nguyễn Văn A",
      "email": "nguyenvana@email.com",
      "dateOfBirth": "1990-01-01",
      "gender": "MALE",
      "citizenId": "123456789",
      "address": "123 Đường ABC, Quận 1, TP.HCM",
      "isDeleted": false,
      "createAt": "2024-01-01T10:00:00",
      "updateAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **4. 📊 GET /api/customers - Danh sách khách hàng (có phân trang, filter)**

**Mô tả:** Lấy danh sách khách hàng với khả năng phân trang, sắp xếp và lọc

**Query Parameters:**
- `fullName` (optional): Lọc theo tên
- `phoneNumber` (optional): Lọc theo số điện thoại
- `citizenId` (optional): Lọc theo CCCD
- `email` (optional): Lọc theo email
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng mỗi trang
- `sortBy` (default: "id"): Sắp xếp theo trường
- `sortDirection` (default: "asc"): Hướng sắp xếp

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

### **5. 🏦 GET /api/customers/{id}/accounts - Lấy tài khoản của khách hàng**

**Mô tả:** Lấy danh sách tất cả tài khoản của một khách hàng

**Path Parameters:**
- `id`: ID của khách hàng

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

## 👨‍💼 STAFF APIs

### **1. 📝 POST /api/staff/register - Đăng ký nhân viên**

**Mô tả:** Tạo tài khoản nhân viên mới

**Request Body:**
```json
{
  "phoneNumber": "0987654321",
  "password": "password123",
  "fullName": "Trần Thị B",
  "email": "tranthib@vietbank.com",
  "employeeCode": "EMP001",
  "departmentId": 1,
  "positionId": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Staff registered successfully",
  "data": {
    "id": 1,
    "phoneNumber": "0987654321",
    "fullName": "Trần Thị B",
    "email": "tranthib@vietbank.com",
    "employeeCode": "EMP001",
    "departmentName": "Phòng Giao dịch",
    "positionName": "Nhân viên",
    "positionLevel": 1,
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **2. 📊 GET /api/staff - Danh sách nhân viên**

**Mô tả:** Lấy danh sách nhân viên với phân trang và sắp xếp

**Query Parameters:**
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng mỗi trang
- `sortBy` (default: "id"): Sắp xếp theo trường
- `sortDirection` (default: "asc"): Hướng sắp xếp

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "content": [...],
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5,
    "first": true,
    "last": false
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **3. 🔍 GET /api/staff/{id} - Thông tin nhân viên**

**Mô tả:** Lấy thông tin chi tiết của một nhân viên

**Path Parameters:**
- `id`: ID của nhân viên

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "phoneNumber": "0987654321",
    "fullName": "Trần Thị B",
    "email": "tranthib@vietbank.com",
    "employeeCode": "EMP001",
    "departmentName": "Phòng Giao dịch",
    "positionName": "Nhân viên",
    "positionLevel": 1,
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **4. ✏️ PUT /api/staff/{id} - Cập nhật thông tin nhân viên**

**Mô tả:** Cập nhật thông tin nhân viên

**Path Parameters:**
- `id`: ID của nhân viên

**Request Body:**
```json
{
  "fullName": "Trần Thị B Updated",
  "email": "tranthib.updated@vietbank.com",
  "employeeCode": "EMP001",
  "departmentId": 2,
  "positionId": 2,
  "isActive": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Staff updated successfully",
  "data": {
    "id": 1,
    "phoneNumber": "0987654321",
    "fullName": "Trần Thị B Updated",
    "email": "tranthib.updated@vietbank.com",
    "employeeCode": "EMP001",
    "departmentName": "Phòng Kế toán",
    "positionName": "Trưởng phòng",
    "positionLevel": 3,
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **5. 🗑️ DELETE /api/staff/{id} - Vô hiệu hóa nhân viên**

**Mô tả:** Vô hiệu hóa nhân viên bằng cách set isActive = false

**Path Parameters:**
- `id`: ID của nhân viên

**Response:**
```json
{
  "success": true,
  "message": "Staff deactivated successfully",
  "data": null,
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **6. 🏢 GET /api/staff/department/{departmentId} - Nhân viên theo phòng ban**

**Mô tả:** Lấy danh sách nhân viên theo phòng ban

**Path Parameters:**
- `departmentId`: ID của phòng ban

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "phoneNumber": "0987654321",
      "fullName": "Trần Thị B",
      "email": "tranthib@vietbank.com",
      "employeeCode": "EMP001",
      "departmentName": "Phòng Giao dịch",
      "positionName": "Nhân viên",
      "positionLevel": 1,
      "isActive": true,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 🎯 TÍNH NĂNG NỔI BẬT

### **✅ Customer APIs:**
- ✅ **Cập nhật thông tin** với validation đầy đủ
- ✅ **Soft delete** an toàn
- ✅ **Tìm kiếm nâng cao** theo nhiều tiêu chí
- ✅ **Phân trang và sắp xếp** thông minh
- ✅ **Lấy tài khoản** của khách hàng
- ✅ **Dynamic filtering** với JPA Specification

### **✅ Staff APIs:**
- ✅ **Đăng ký nhân viên** với validation
- ✅ **Quản lý nhân viên** đầy đủ CRUD
- ✅ **Phân trang và sắp xếp** hiệu quả
- ✅ **Vô hiệu hóa nhân viên** (soft delete)
- ✅ **Lọc theo phòng ban** tiện lợi
- ✅ **Kiểm tra trùng lặp** employee code

### **🔧 Công nghệ sử dụng:**
- Spring Boot 3.5.5
- Spring Data JPA với Specification
- Lombok cho code generation
- Validation API
- DTO pattern
- RESTful API design
- Pagination và Sorting

---

## 📊 DTOs ĐÃ TẠO

### **Customer DTOs:**
- `UpdateCustomerRequest` - Cập nhật khách hàng
- `CustomerSearchRequest` - Tìm kiếm khách hàng
- `CustomerResponse` - Response khách hàng (đã có)

### **Staff DTOs:**
- `StaffRegistrationRequest` - Đăng ký nhân viên
- `UpdateStaffRequest` - Cập nhật nhân viên
- `StaffResponse` - Response nhân viên

---

## 🚀 CÁCH SỬ DỤNG

### **Ví dụ tìm kiếm khách hàng:**
```bash
GET /api/customers/search?fullName=Nguyễn&phoneNumber=0123
```

### **Ví dụ cập nhật khách hàng:**
```bash
PUT /api/customers/1
Content-Type: application/json

{
  "fullName": "Nguyễn Văn A Updated",
  "email": "nguyenvana.updated@email.com"
}
```

### **Ví dụ đăng ký nhân viên:**
```bash
POST /api/staff/register
Content-Type: application/json

{
  "phoneNumber": "0987654321",
  "password": "password123",
  "fullName": "Trần Thị B",
  "email": "tranthib@vietbank.com",
  "employeeCode": "EMP001",
  "departmentId": 1,
  "positionId": 1
}
```

### **Ví dụ lấy nhân viên theo phòng ban:**
```bash
GET /api/staff/department/1
```

---

## 🔒 BẢO MẬT VÀ VALIDATION

### **Validation Rules:**
- ✅ Tên phải có 2-100 ký tự
- ✅ Email phải đúng format
- ✅ CCCD phải có 9-12 chữ số
- ✅ Số điện thoại phải có 10-11 chữ số
- ✅ Employee code phải unique
- ✅ Kiểm tra department và position tồn tại

### **Exception Handling:**
- ✅ `ResourceNotFoundException` - Không tìm thấy resource
- ✅ `DuplicateResourceException` - Trùng lặp dữ liệu
- ✅ `MethodArgumentNotValidException` - Validation errors

---

## 📈 BƯỚC TIẾP THEO

1. **Authentication APIs** - Đăng nhập/đăng xuất
2. **Department & Position APIs** - Quản lý tổ chức
3. **Reporting APIs** - Báo cáo thống kê
4. **Notification APIs** - Thông báo
5. **Audit Logging** - Ghi log chi tiết

---

## 🎉 TỔNG KẾT

**Customer & Staff APIs đã hoàn thành 100%** với đầy đủ tính năng:
- ✅ 5 Customer API endpoints
- ✅ 6 Staff API endpoints
- ✅ Validation và exception handling
- ✅ Pagination và sorting
- ✅ Search và filtering
- ✅ Soft delete
- ✅ Code quality cao

**Sẵn sàng cho production!** 🚀
