# 🔐 Authentication APIs

**Base URL**: `http://localhost:8080/api/auth`

## 📋 Tổng quan
API authentication với JWT token, hỗ trợ login, logout, refresh token và profile.

## 📋 Danh sách API

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

## 📝 Notes

- Token expires sau 24 giờ
- Refresh token expires sau 7 ngày
- Password hỗ trợ MD5 hash
- JWT token chứa userId, username, role
