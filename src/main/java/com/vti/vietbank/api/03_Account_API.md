# 🏦 Account APIs

**Base URL**: `http://localhost:8080/api/accounts`

## 📋 Tổng quan
API quản lý tài khoản: Mở, cập nhật, đóng tài khoản với hỗ trợ Account ID và Account Number.

## 📋 Danh sách API

### GET /api/accounts
**Danh sách tài khoản**

**Query Parameters:**
- `accountNumber` (String)
- `accountName` (String)
- `customerId` (Integer)
- `accountTypeId` (Integer)
- `status` (String)
- `minBalance` (BigDecimal)
- `maxBalance` (BigDecimal)
- `page` (Integer, default: 0)
- `size` (Integer, default: 10)
- `sortBy` (String, default: "id")
- `sortDirection` (String, default: "asc")

**Response:** PageResponse<AccountResponse>

---

### POST /api/accounts
**Mở tài khoản**

**Request:**
```json
{
  "customerId": 1,
  "accountTypeId": 1,
  "accountName": "NGUYEN VAN A"
}
```

**Response:** AccountResponse

---

### GET /api/accounts/{accountNumber}
**Chi tiết tài khoản**

**Parameters:** `accountNumber` (String)

**Response:** AccountResponse

---

### PUT /api/accounts/{id}
**Cập nhật tài khoản**

**Request:**
```json
{
  "accountName": "NGUYEN VAN A UPDATED",
  "status": "ACTIVE"
}
```

**Response:** AccountResponse

---

### DELETE /api/accounts/{id}
**Đóng tài khoản**

**Response:**
```json
{
  "success": true,
  "message": "Account closed successfully"
}
```

---

### GET /api/accounts/customer/{customerId}
**Tài khoản theo customer**

**Parameters:** `customerId` (Integer)

**Response:** List<AccountResponse>

---

### GET /api/accounts/{id}/balance
**Số dư theo ID**

**Parameters:** `id` (Integer)

**Response:**
```json
{
  "success": true,
  "data": 1500000
}
```

