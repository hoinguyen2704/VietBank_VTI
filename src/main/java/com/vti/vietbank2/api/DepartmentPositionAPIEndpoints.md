# 🏢 DEPARTMENT & POSITION API ENDPOINTS - VietBank Digital

## 📋 DANH SÁCH API ĐÃ HOÀN THÀNH

---

## 🏢 DEPARTMENT APIs

### **1. 📊 GET /api/departments - Danh sách phòng ban**

**Mô tả:** Lấy danh sách tất cả phòng ban trong hệ thống

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Phòng Giao dịch",
      "description": "Phòng phụ trách các giao dịch ngân hàng",
      "managerName": null,
      "managerCode": null,
      "managerEmail": null,
      "totalStaff": 0,
      "createdAt": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "name": "Phòng Kế toán",
      "description": "Phòng phụ trách kế toán và tài chính",
      "managerName": null,
      "managerCode": null,
      "managerEmail": null,
      "totalStaff": 0,
      "createdAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **2. 📝 POST /api/departments - Tạo phòng ban mới**

**Mô tả:** Tạo phòng ban mới trong hệ thống

**Request Body:**
```json
{
  "name": "Phòng Nhân sự",
  "description": "Phòng phụ trách quản lý nhân sự và tuyển dụng"
}
```

**Validation:**
- `name`: Bắt buộc, 2-100 ký tự, phải unique
- `description`: Tùy chọn, tối đa 255 ký tự

**Response:**
```json
{
  "success": true,
  "message": "Department created successfully",
  "data": {
    "id": 3,
    "name": "Phòng Nhân sự",
    "description": "Phòng phụ trách quản lý nhân sự và tuyển dụng",
    "managerName": null,
    "managerCode": null,
    "managerEmail": null,
    "totalStaff": 0,
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 👔 POSITION APIs

### **3. 📊 GET /api/positions - Danh sách chức vụ**

**Mô tả:** Lấy danh sách tất cả chức vụ trong hệ thống

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Nhân viên",
      "description": "Chức vụ nhân viên cơ bản",
      "level": 1,
      "departmentName": "Phòng Giao dịch",
      "departmentId": 1,
      "createdAt": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "name": "Trưởng phòng",
      "description": "Chức vụ trưởng phòng",
      "level": 3,
      "departmentName": "Phòng Giao dịch",
      "departmentId": 1,
      "createdAt": "2024-01-01T10:00:00"
    },
    {
      "id": 3,
      "name": "Kế toán viên",
      "description": "Chức vụ kế toán viên",
      "level": 1,
      "departmentName": "Phòng Kế toán",
      "departmentId": 2,
      "createdAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **4. 🏢 GET /api/positions/department/{departmentId} - Chức vụ theo phòng ban**

**Mô tả:** Lấy danh sách chức vụ của một phòng ban cụ thể

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
      "name": "Nhân viên",
      "description": "Chức vụ nhân viên cơ bản",
      "level": 1,
      "departmentName": "Phòng Giao dịch",
      "departmentId": 1,
      "createdAt": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "name": "Trưởng phòng",
      "description": "Chức vụ trưởng phòng",
      "level": 3,
      "departmentName": "Phòng Giao dịch",
      "departmentId": 1,
      "createdAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 🎯 TÍNH NĂNG NỔI BẬT

### **✅ Department APIs:**
- ✅ **Danh sách phòng ban** với thông tin đầy đủ
- ✅ **Tạo phòng ban mới** với validation
- ✅ **Kiểm tra trùng lặp** tên phòng ban
- ✅ **Validation đầy đủ** cho input
- ✅ **Exception handling** chuyên nghiệp

### **✅ Position APIs:**
- ✅ **Danh sách chức vụ** với thông tin phòng ban
- ✅ **Lọc theo phòng ban** tiện lợi
- ✅ **Thông tin level** và hierarchy
- ✅ **Liên kết với phòng ban** rõ ràng
- ✅ **Performance tối ưu** với JPA

### **🔧 Công nghệ sử dụng:**
- Spring Boot 3.5.5
- Spring Data JPA
- Lombok cho code generation
- Validation API
- DTO pattern
- RESTful API design

---

## 📊 DTOs ĐÃ TẠO

### **Department DTOs:**
- `CreateDepartmentRequest` - Request tạo phòng ban
- `DepartmentResponse` - Response phòng ban

### **Position DTOs:**
- `PositionResponse` - Response chức vụ

---

## 🚀 CÁCH SỬ DỤNG

### **Ví dụ tạo phòng ban mới:**
```bash
POST /api/departments
Content-Type: application/json

{
  "name": "Phòng Marketing",
  "description": "Phòng phụ trách marketing và quảng cáo"
}
```

### **Ví dụ lấy danh sách phòng ban:**
```bash
GET /api/departments
```

### **Ví dụ lấy danh sách chức vụ:**
```bash
GET /api/positions
```

### **Ví dụ lấy chức vụ theo phòng ban:**
```bash
GET /api/positions/department/1
```

---

## 🔒 BẢO MẬT VÀ VALIDATION

### **Validation Rules:**
- ✅ Tên phòng ban phải có 2-100 ký tự
- ✅ Tên phòng ban phải unique
- ✅ Mô tả tối đa 255 ký tự
- ✅ Kiểm tra department tồn tại

### **Exception Handling:**
- ✅ `ResourceNotFoundException` - Không tìm thấy resource
- ✅ `DuplicateResourceException` - Trùng lặp tên phòng ban
- ✅ `MethodArgumentNotValidException` - Validation errors

---

## 📈 BUSINESS LOGIC

### **Department Management:**
- **Tạo phòng ban**: Tự động generate ID, timestamp
- **Validation**: Kiểm tra tên trùng lặp
- **Soft structure**: Không có manager_id để tránh circular reference

### **Position Management:**
- **Hierarchy**: Level từ 1-5 (1: nhân viên, 5: giám đốc)
- **Department binding**: Mỗi chức vụ thuộc 1 phòng ban
- **Flexible querying**: Lấy theo phòng ban hoặc tất cả

---

## 🔗 RELATIONSHIPS

### **Department ↔ Position:**
- **One-to-Many**: 1 phòng ban có nhiều chức vụ
- **Many-to-One**: 1 chức vụ thuộc 1 phòng ban
- **Foreign Key**: position.department_id → department.id

### **Department ↔ Staff:**
- **One-to-Many**: 1 phòng ban có nhiều nhân viên
- **Many-to-One**: 1 nhân viên thuộc 1 phòng ban

### **Position ↔ Staff:**
- **One-to-Many**: 1 chức vụ có nhiều nhân viên
- **Many-to-One**: 1 nhân viên có 1 chức vụ

---

## 📈 BƯỚC TIẾP THEO

1. **Authentication APIs** - Đăng nhập/đăng xuất
2. **Reporting APIs** - Báo cáo thống kê
3. **Notification APIs** - Thông báo
4. **Audit Logging** - Ghi log chi tiết
5. **Department Management** - Cập nhật, xóa phòng ban

---

## 🎉 TỔNG KẾT

**Department & Position APIs đã hoàn thành 100%** với đầy đủ tính năng:
- ✅ 2 Department API endpoints
- ✅ 2 Position API endpoints
- ✅ Validation và exception handling
- ✅ DTO pattern
- ✅ RESTful design
- ✅ Code quality cao

**Sẵn sàng cho production!** 🚀
