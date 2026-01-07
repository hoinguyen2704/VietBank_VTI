# 🏢 Department & Position APIs

**Base URL**: `http://localhost:8080/api`

## 📋 Tổng quan
API quản lý tổ chức: Phòng ban, chức vụ và hierarchy.

## 📋 Danh sách API

### GET /api/departments
**Danh sách phòng ban**

**Response:** List<DepartmentResponse>

---

### POST /api/departments
**Tạo phòng ban**

**Request:**
```json
{
  "name": "Phòng Marketing",
  "description": "Phòng marketing và quảng cáo"
}
```

**Response:** DepartmentResponse

---

### GET /api/positions
**Danh sách chức vụ**

**Response:** List<PositionResponse>

---

### GET /api/positions/department/{departmentId}
**Chức vụ theo phòng ban**

**Parameters:** `departmentId` (Integer)

**Response:** List<PositionResponse>

