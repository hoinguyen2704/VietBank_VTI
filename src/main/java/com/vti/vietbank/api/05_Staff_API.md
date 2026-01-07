
# 👔 Staff APIs

**Base URL**: `http://localhost:8080/api/staff`

## 📋 Tổng quan
API quản lý nhân viên: Đăng ký, quản lý nhân viên theo phòng ban và chức vụ.

## 📋 Danh sách API

### POST /api/staff/register
**Đăng ký nhân viên**

**Request:**
```json
{
  "phoneNumber": "0900000020",
  "password": "123456",
  "fullName": "Nhan Vien Moi",
  "employeeCode": "EMP999",
  "departmentId": 1,
  "positionId": 1
}
```

**Response:** StaffResponse

---

### GET /api/staff
**Danh sách nhân viên**

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 10)
- `sortBy` (default: "id")
- `sortDirection` (default: "asc")

**Response:** PageResponse<StaffResponse>

---

### GET /api/staff/{id}
**Chi tiết nhân viên**

**Parameters:** `id` (Integer)

**Response:** StaffResponse

---

### PUT /api/staff/{id}
**Cập nhật nhân viên**

**Request:**
```json
{
  "fullName": "Updated Name",
  "departmentId": 2,
  "positionId": 2
}
```

**Response:** StaffResponse

---

### DELETE /api/staff/{id}
**Xóa nhân viên**

**Response:**
```json
{
  "success": true,
  "message": "Staff deleted successfully"
}
```

---

### GET /api/staff/department/{departmentId}
**Nhân viên theo phòng ban**

**Parameters:** `departmentId` (Integer)

**Response:** List<StaffResponse>

