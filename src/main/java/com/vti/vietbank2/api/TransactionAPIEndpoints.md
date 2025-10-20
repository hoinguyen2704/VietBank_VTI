# 💰 TRANSACTION API ENDPOINTS - VietBank Digital

## 📋 DANH SÁCH API ĐÃ HOÀN THÀNH

### **1. 💳 POST /api/transactions/deposit - Nạp tiền vào tài khoản**

**Mô tả:** Nạp tiền vào tài khoản với số tiền và mô tả

**Request Body:**
```json
{
  "accountId": 1,
  "amount": 1000000.00,
  "description": "Nạp tiền từ ATM",
  "createdBy": 1
}
```

**Validation:**
- `accountId`: Bắt buộc, phải tồn tại
- `amount`: Bắt buộc, phải > 0, tối thiểu 1,000 VND
- `description`: Tùy chọn
- `createdBy`: Bắt buộc, ID nhân viên xử lý

**Response:**
```json
{
  "success": true,
  "message": "Deposit completed successfully",
  "data": {
    "id": 1,
    "transactionCode": "TXN1703123456789ABCDEFGH",
    "transactionType": "DEPOSIT",
    "accountId": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "relatedAccountId": null,
    "relatedAccountNumber": null,
    "relatedCustomerName": null,
    "amount": 1000000.00,
    "fee": 0.00,
    "balanceAfter": 1000000.00,
    "description": "Nạp tiền từ ATM",
    "createdByName": "0123456789",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **2. 💸 POST /api/transactions/withdraw - Rút tiền từ tài khoản**

**Mô tả:** Rút tiền từ tài khoản với kiểm tra số dư

**Request Body:**
```json
{
  "accountId": 1,
  "amount": 500000.00,
  "description": "Rút tiền mặt",
  "createdBy": 1
}
```

**Validation:**
- `accountId`: Bắt buộc, phải tồn tại và ACTIVE
- `amount`: Bắt buộc, phải > 0, tối thiểu 1,000 VND
- Kiểm tra số dư đủ để rút
- `createdBy`: Bắt buộc, ID nhân viên xử lý

**Response:**
```json
{
  "success": true,
  "message": "Withdrawal completed successfully",
  "data": {
    "id": 2,
    "transactionCode": "TXN1703123456789ABCDEFGH",
    "transactionType": "WITHDRAWAL",
    "accountId": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "relatedAccountId": null,
    "relatedAccountNumber": null,
    "relatedCustomerName": null,
    "amount": 500000.00,
    "fee": 0.00,
    "balanceAfter": 500000.00,
    "description": "Rút tiền mặt",
    "createdByName": "0123456789",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **3. 🔄 POST /api/transactions/transfer - Chuyển khoản giữa các tài khoản**

**Mô tả:** Chuyển tiền từ tài khoản này sang tài khoản khác

**Request Body:**
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 200000.00,
  "description": "Chuyển tiền cho bạn",
  "createdBy": 1
}
```

**Validation:**
- `fromAccountId`: Bắt buộc, phải tồn tại và ACTIVE
- `toAccountId`: Bắt buộc, phải tồn tại và ACTIVE
- `amount`: Bắt buộc, phải > 0, tối thiểu 1,000 VND
- Kiểm tra số dư đủ để chuyển
- Không thể chuyển cho chính mình
- `createdBy`: Bắt buộc, ID nhân viên xử lý

**Response:**
```json
{
  "success": true,
  "message": "Transfer completed successfully",
  "data": {
    "id": 3,
    "transactionCode": "TXN1703123456789ABCDEFGH",
    "transactionType": "TRANSFER_OUT",
    "accountId": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "relatedAccountId": 2,
    "relatedAccountNumber": "0987654321",
    "relatedCustomerName": "Trần Thị B",
    "amount": 200000.00,
    "fee": 0.00,
    "balanceAfter": 300000.00,
    "description": "Chuyển tiền cho bạn",
    "createdByName": "0123456789",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **4. 📊 GET /api/transactions/account/{accountId} - Lịch sử giao dịch theo tài khoản**

**Mô tả:** Lấy danh sách tất cả giao dịch của một tài khoản

**Path Parameters:**
- `accountId`: ID của tài khoản

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "transactionCode": "TXN1703123456789ABCDEFGH",
      "transactionType": "DEPOSIT",
      "accountId": 1,
      "accountNumber": "1234567890",
      "customerName": "Nguyễn Văn A",
      "relatedAccountId": null,
      "relatedAccountNumber": null,
      "relatedCustomerName": null,
      "amount": 1000000.00,
      "fee": 0.00,
      "balanceAfter": 1000000.00,
      "description": "Nạp tiền từ ATM",
      "createdByName": "0123456789",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **5. 👤 GET /api/transactions/customer/{customerId} - Lịch sử giao dịch theo khách hàng**

**Mô tả:** Lấy danh sách tất cả giao dịch của một khách hàng (tất cả tài khoản)

**Path Parameters:**
- `customerId`: ID của khách hàng

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "transactionCode": "TXN1703123456789ABCDEFGH",
      "transactionType": "DEPOSIT",
      "accountId": 1,
      "accountNumber": "1234567890",
      "customerName": "Nguyễn Văn A",
      "relatedAccountId": null,
      "relatedAccountNumber": null,
      "relatedCustomerName": null,
      "amount": 1000000.00,
      "fee": 0.00,
      "balanceAfter": 1000000.00,
      "description": "Nạp tiền từ ATM",
      "createdByName": "0123456789",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### **6. 🔍 GET /api/transactions/{id} - Chi tiết giao dịch**

**Mô tả:** Lấy thông tin chi tiết của một giao dịch

**Path Parameters:**
- `id`: ID của giao dịch

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "transactionCode": "TXN1703123456789ABCDEFGH",
    "transactionType": "DEPOSIT",
    "accountId": 1,
    "accountNumber": "1234567890",
    "customerName": "Nguyễn Văn A",
    "relatedAccountId": null,
    "relatedAccountNumber": null,
    "relatedCustomerName": null,
    "amount": 1000000.00,
    "fee": 0.00,
    "balanceAfter": 1000000.00,
    "description": "Nạp tiền từ ATM",
    "createdByName": "0123456789",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 🎯 TÍNH NĂNG NỔI BẬT

### **✅ Đã hoàn thành:**
- ✅ **Nạp tiền** với validation đầy đủ
- ✅ **Rút tiền** với kiểm tra số dư
- ✅ **Chuyển khoản** giữa các tài khoản
- ✅ **Lịch sử giao dịch** theo tài khoản
- ✅ **Lịch sử giao dịch** theo khách hàng
- ✅ **Chi tiết giao dịch** theo ID
- ✅ **Transaction Code** tự động sinh
- ✅ **Balance tracking** (trước và sau giao dịch)
- ✅ **Validation** đầy đủ
- ✅ **Exception handling** chuyên nghiệp
- ✅ **Transaction management** đảm bảo ACID

### **🔧 Công nghệ sử dụng:**
- Spring Boot 3.5.5
- Spring Data JPA
- Lombok cho code generation
- Validation API
- DTO pattern
- RESTful API design
- Transaction Management (@Transactional)

### **📊 DTOs đã tạo:**
- `DepositRequest` - Request nạp tiền
- `WithdrawalRequest` - Request rút tiền
- `TransferRequest` - Request chuyển khoản
- `TransactionResponse` - Response giao dịch
- `InsufficientBalanceException` - Exception số dư không đủ

---

## 🚀 CÁCH SỬ DỤNG

### **Ví dụ nạp tiền:**
```bash
POST /api/transactions/deposit
Content-Type: application/json

{
  "accountId": 1,
  "amount": 1000000.00,
  "description": "Nạp tiền từ ATM",
  "createdBy": 1
}
```

### **Ví dụ rút tiền:**
```bash
POST /api/transactions/withdraw
Content-Type: application/json

{
  "accountId": 1,
  "amount": 500000.00,
  "description": "Rút tiền mặt",
  "createdBy": 1
}
```

### **Ví dụ chuyển khoản:**
```bash
POST /api/transactions/transfer
Content-Type: application/json

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 200000.00,
  "description": "Chuyển tiền cho bạn",
  "createdBy": 1
}
```

### **Ví dụ lấy lịch sử:**
```bash
GET /api/transactions/account/1
GET /api/transactions/customer/1
GET /api/transactions/1
```

---

## 🔒 BẢO MẬT VÀ VALIDATION

### **Validation Rules:**
- ✅ Số tiền tối thiểu: 1,000 VND
- ✅ Kiểm tra tài khoản tồn tại và ACTIVE
- ✅ Kiểm tra số dư đủ cho rút/chuyển
- ✅ Không thể chuyển cho chính mình
- ✅ Kiểm tra nhân viên tồn tại

### **Exception Handling:**
- ✅ `ResourceNotFoundException` - Tài khoản/khách hàng không tồn tại
- ✅ `InsufficientBalanceException` - Số dư không đủ
- ✅ `IllegalArgumentException` - Tài khoản không active
- ✅ `MethodArgumentNotValidException` - Validation errors

---

## 📈 BƯỚC TIẾP THEO

1. **Authentication APIs** - Đăng nhập/đăng xuất
2. **Staff Management APIs** - Quản lý nhân viên
3. **Reporting APIs** - Báo cáo thống kê
4. **Notification APIs** - Thông báo giao dịch
5. **Audit Logging** - Ghi log chi tiết

---

## 🎉 TỔNG KẾT

**Transaction APIs đã hoàn thành 100%** với đầy đủ tính năng:
- ✅ 6 API endpoints chính
- ✅ Validation và exception handling
- ✅ Transaction management
- ✅ DTO pattern
- ✅ RESTful design
- ✅ Code quality cao

**Sẵn sàng cho production!** 🚀
