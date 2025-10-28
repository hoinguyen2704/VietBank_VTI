# 📚 VietBank2 API Documentation

**Base URL**: `http://localhost:8080/api`

## 📋 Mục lục

- [Authentication APIs](#-authentication-apis) - Login, Logout, Refresh Token
- [Transaction APIs](#-transaction-apis) - Deposit, Withdrawal, Transfer
- [Account APIs](#-account-apis) - Account Management
- [Customer APIs](#-customer-apis) - Customer Management
- [Staff APIs](#-staff-apis) - Staff Management
- [Department APIs](#-department-apis) - Department Management
- [Position APIs](#-position-apis) - Position Management
- [Phân quyền](#-phân-quyền) - Role-based Access Control

---

## 🔐 Authentication APIs

### POST /api/auth/login
**Đăng nhập**

**Request:**
```json
{
  "phoneNumber": "0900000001",
  "password": "123456"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "userId": 1,
    "username": "0900000001",
    "role": "CUSTOMER"
  }
}
```

---

### POST /api/auth/refresh
**Làm mới token**

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "new_token_here...",
    "refreshToken": "same_refresh_token..."
  }
}
```

---

### POST /api/auth/logout
**Đăng xuất**

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

---

### GET /api/auth/profile
**Thông tin profile**

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "data": {
    "username": "0900000001",
    "authorities": ["ROLE_CUSTOMER"]
  }
}
```

---

## 💰 Transaction APIs

### POST /api/transactions/deposit
**Nạp tiền**

**Authentication:** Required (STAFF only)

**Request:**
```json
{
  "accountNumber": "1000000000001",
  "amount": 1000000,
  "description": "Nạp tiền tại quầy",
  "createdBy": 3
}
```

**Response:**
```json
{
  "success": true,
  "message": "Deposit completed successfully",
  "data": {
    "id": 1,
    "transactionCode": "TXN123456789",
    "transactionType": "DEPOSIT",
    "accountNumber": "1000000000001",
    "amount": 1000000,
    "balanceAfter": 2000000,
    "createdByName": "LE VAN C"
  }
}
```

---

### POST /api/transactions/withdraw
**Rút tiền**

**Authentication:** Required (CUSTOMER own accounts, STAFF all accounts)

**Request:**
```json
{
  "accountNumber": "1000000000001",
  "amount": 500000,
  "description": "Rút tiền ATM",
  "createdBy": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Withdrawal completed successfully",
  "data": {
    "id": 2,
    "transactionCode": "TXN987654321",
    "transactionType": "WITHDRAWAL",
    "accountNumber": "1000000000001",
    "amount": 500000,
    "balanceAfter": 1500000,
    "createdByName": "NGUYEN VAN A"
  }
}
```

---

### POST /api/transactions/transfer
**Chuyển khoản**

**Authentication:** Required (CUSTOMER from own accounts, STAFF all accounts)

**Request:**
```json
{
  "fromAccountNumber": "1000000000001",
  "toAccountNumber": "1000000000002",
  "amount": 300000,
  "description": "Chuyển khoản",
  "createdBy": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Transfer completed successfully",
  "data": {
    "id": 3,
    "transactionCode": "TXN456789012",
    "transactionType": "TRANSFER_OUT",
    "accountNumber": "1000000000001",
    "relatedAccountNumber": "1000000000002",
    "amount": 300000,
    "balanceAfter": 1200000,
    "createdByName": "NGUYEN VAN A"
  }
}
```

---

### GET /api/transactions/{id}
**Chi tiết giao dịch**

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "transactionCode": "TXN123456789",
    "transactionType": "DEPOSIT",
    "accountNumber": "1000000000001",
    "amount": 1000000,
    "balanceAfter": 2000000,
    "createdAt": "2024-01-15T10:00:00"
  }
}
```

---

### GET /api/transactions/account/{accountId}
**Lịch sử theo account ID**

**Parameters:** `accountId` (Integer)

**Response:** List of TransactionResponse

---

### GET /api/transactions/account-number/{accountNumber}
**Lịch sử theo số tài khoản**

**Parameters:** `accountNumber` (String)

**Response:** List of TransactionResponse

---

### GET /api/transactions/customer/{customerId}
**Lịch sử theo customer**

**Parameters:** `customerId` (Integer)

**Response:** List of TransactionResponse

---

## 🏦 Account APIs

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

---

## 👥 Customer APIs

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

## 👔 Staff APIs

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

---

## 🏢 Department & Position APIs

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

---

## 🔑 Role Management

### GET /api/roles
**Danh sách vai trò**

**Response:** List of Roles

**Roles:**
- `CUSTOMER` - Khách hàng
- `STAFF` - Nhân viên
- `ADMIN` - Quản trị viên

## 🔐 Phân quyền

| Endpoint | CUSTOMER | STAFF | ADMIN |
|----------|----------|-------|-------|
| `/api/auth/**` | ✅ | ✅ | ✅ |
| `/api/accounts/**` | ✅ | ✅ | ✅ |
| `/api/transactions/**` | ✅* | ✅ | ✅ |
| `/api/customers/**` | ❌ | ✅ | ✅ |
| `/api/staff/**` | ❌ | ❌ | ✅ |
| `/api/departments/**` | ❌ | ✅ | ✅ |
| `/api/positions/**` | ❌ | ✅ | ✅ |

*Customer chỉ có thể truy cập own accounts

---

## 🎯 Account Ownership Rules

- **CUSTOMER**: Chỉ có thể withdraw/transfer từ tài khoản của chính họ
- **STAFF**: Có thể truy cập tất cả tài khoản
- **ADMIN**: Full access

---

## 📝 Notes

- Tất cả timestamps sử dụng ISO 8601 format
- Số tiền sử dụng BigDecimal (precision 15, scale 2)
- Token expires sau 24 giờ
- Refresh token expires sau 7 ngày
