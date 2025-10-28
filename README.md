# 🏦 VietBank2 - Banking Management System

Hệ thống quản lý ngân hàng số hiện đại với Spring Boot 3.5.6

## 📋 Tổng quan

VietBank2 là hệ thống quản lý ngân hàng số với đầy đủ tính năng:
- ✅ Authentication & Authorization với JWT
- ✅ Account Management
- ✅ Transaction Management (Deposit, Withdrawal, Transfer)
- ✅ Customer & Staff Management
- ✅ Role-based Access Control
- ✅ Account Ownership Security

## 🚀 Tech Stack

- **Backend**: Spring Boot 3.5.6
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **ORM**: Hibernate/JPA
- **Build**: Maven
- **Java**: 21

## 📚 Documentation

### API Documentation
- **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)** - Complete API reference
- **[01_Authentication_API.md](./src/main/java/com/vti/vietbank2/api/01_Authentication_API.md)** - Login, Logout, JWT
- **[02_Transaction_API.md](./src/main/java/com/vti/vietbank2/api/02_Transaction_API.md)** - Deposit, Withdrawal, Transfer
- **[03_Account_API.md](./src/main/java/com/vti/vietbank2/api/03_Account_API.md)** - Account Management
- **[04_Customer_API.md](./src/main/java/com/vti/vietbank2/api/04_Customer_API.md)** - Customer Management
- **[05_Staff_API.md](./src/main/java/com/vti/vietbank2/api/05_Staff_API.md)** - Staff Management
- **[06_Department_Position_API.md](./src/main/java/com/vti/vietbank2/api/06_Department_Position_API.md)** - Org Management
- **[07_Security_Roles.md](./src/main/java/com/vti/vietbank2/api/07_Security_Roles.md)** - Security & Permissions

## 🔐 Authentication

### Login
```bash
POST /api/auth/login
{
  "phoneNumber": "0900000001",
  "password": "123456"
}
```

### Sử dụng token
```bash
GET /api/accounts
Headers: Authorization: Bearer <token>
```

## 💰 Quick Start

### 1. Setup Database
```bash
mysql -u root -p < database/vietbank_schema.sql
```

### 2. Run Application
```bash
mvn spring-boot:run
```

### 3. Test API
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber": "0900000001", "password": "123456"}'
```

## 🔑 Default Accounts

### Customer
- Phone: `0900000001`, Password: `123456`
- Phone: `0900000002`, Password: `123456`

### Staff
- Phone: `0900000003`, Password: `123456`
- Phone: `0900000004`, Password: `123456`

### Admin
- Phone: `0900000009`, Password: `123456`

## 📖 API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/refresh` - Refresh token
- `POST /api/auth/logout` - Đăng xuất
- `GET /api/auth/profile` - Thông tin profile

### Transactions
- `POST /api/transactions/deposit` - Nạp tiền
- `POST /api/transactions/withdraw` - Rút tiền
- `POST /api/transactions/transfer` - Chuyển khoản
- `GET /api/transactions/{id}` - Chi tiết giao dịch

### Accounts
- `GET /api/accounts` - Danh sách tài khoản
- `POST /api/accounts` - Mở tài khoản
- `GET /api/accounts/{accountNumber}` - Chi tiết tài khoản
- `PUT /api/accounts/{id}` - Cập nhật tài khoản

## 🔒 Security Features

### Account Ownership
- ✅ Customer chỉ có thể truy cập account của chính họ
- ✅ Staff có thể truy cập tất cả accounts
- ✅ Admin full access

### Role-based Access
- **CUSTOMER**: Quản lý account của chính họ
- **STAFF**: Quản lý customers & transactions
- **ADMIN**: Full system access

## 📝 Project Structure

```
src/main/java/com/vti/vietbank2/
├── controller/     # REST Controllers
├── service/        # Business Logic
├── repository/     # Data Access
├── entity/         # Database Entities
├── dto/            # Data Transfer Objects
├── security/       # Security Config
├── exception/      # Exception Handling
└── util/           # Utility Classes
```

## 🎯 Features

- ✅ JWT Authentication
- ✅ MD5 Password Support
- ✅ Account & Number Support
- ✅ Transaction History
- ✅ Role-based Security
- ✅ Account Ownership Validation
- ✅ Comprehensive Validation

## 📄 License

VietBank2 Banking System

---

**Sẵn sàng cho production!** 🚀
