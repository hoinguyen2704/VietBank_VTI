# 🔐 Security & Role Management

## 🔑 Role Management

### GET /api/roles
**Danh sách vai trò**

**Response:** List of Roles

**Roles:**
- `CUSTOMER` - Khách hàng
- `STAFF` - Nhân viên
- `ADMIN` - Quản trị viên

---

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

### CUSTOMER
- ✅ Chỉ có thể withdraw/transfer từ tài khoản của chính họ
- ✅ Chỉ có thể xem lịch sử giao dịch của chính họ
- ❌ Không thể truy cập thông tin khách hàng khác

### STAFF
- ✅ Có thể truy cập tất cả tài khoản
- ✅ Có thể nạp tiền cho bất kỳ tài khoản nào
- ✅ Có thể xem lịch sử của bất kỳ khách hàng nào
- ✅ Có thể quản lý customers và transactions

### ADMIN
- ✅ Full system access
- ✅ Quản lý staff
- ✅ Quản lý departments và positions
- ✅ Toàn quyền truy cập

---

## 📝 Security Notes

- **JWT Token**: 24 giờ expiration
- **Refresh Token**: 7 ngày expiration
- **Password**: MD5 hash support
- **Account Security**: Ownership validation
- **Role-based**: Spring Security implementation
