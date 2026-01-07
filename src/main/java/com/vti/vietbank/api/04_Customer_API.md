# 👥 Customer APIs

**Base URL**: `http://localhost:8080/api/customers`

## 📋 Tổng quan
API quản lý khách hàng: Đăng ký, cập nhật, tìm kiếm khách hàng.

## 📋 Danh sách API

### GET /api/customers
**Danh sách khách hàng**

**Query Parameters:**
- `fullName` (String)
- `email` (String)
- `phoneNumber` (String)
- `page` (Integer, default: 0)
- `size` (Integer, default: 10)
- `sortBy` (String, default: "id")
- `sortDirection` (String, default: "asc")

**Response:** PageResponse<CustomerResponse>

---

### POST /api/customers/register
**Đăng ký khách hàng**

**Request:**
```json
{
  "phoneNumber": "0900000020",
  "password": "123456",
  "fullName": "TRAN VAN Z",
  "email": "z.tran@example.com",
  "citizenId": "012345677911",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01",
  "address": "123 Street, City"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "id": 1,
    "fullName": "TRAN VAN Z",
    "email": "z.tran@example.com",
    "phoneNumber": "0900000020",
    "citizenId": "012345677911"
  }
}
```

---

### GET /api/customers/{id}
**Chi tiết khách hàng**

**Parameters:** `id` (Integer)

**Response:** CustomerResponse

---

### PUT /api/customers/{id}
**Cập nhật khách hàng**

**Request:**
```json
{
  "fullName": "Updated Name",
  "email": "updated@email.com",
  "address": "New Address"
}
```

**Response:** CustomerResponse

---

### DELETE /api/customers/{id}
**Xóa khách hàng (soft delete)**

**Parameters:** `id` (Integer)

**Response:**
```json
{
  "success": true,
  "message": "Customer deleted successfully"
}
```

---

### GET /api/customers/search
**Tìm kiếm khách hàng**

**Query Parameters:**
- `fullName` (String)
- `phoneNumber` (String)
- `citizenId` (String)
- `email` (String)

**Response:** List<CustomerResponse>
