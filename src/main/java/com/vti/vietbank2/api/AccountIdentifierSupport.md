# 🔄 HỖ TRỢ CẢ ACCOUNT ID VÀ ACCOUNT NUMBER

## 🤔 TẠI SAO HỖ TRỢ CẢ HAI?

### **1. 🔒 BẢO MẬT:**
- **accountId**: Internal ID, không thể đoán được, an toàn hơn
- **accountNumber**: Có thể bị brute force, nhưng user-friendly

### **2. 🚀 PERFORMANCE:**
- **accountId**: PRIMARY KEY, index nhanh nhất
- **accountNumber**: Cần tìm kiếm qua bảng accounts

### **3. 👥 USER EXPERIENCE:**
- **accountId**: Dành cho internal systems
- **accountNumber**: Dành cho end users (dễ nhớ hơn)

### **4. 🛡️ FLEXIBILITY:**
- Hỗ trợ cả 2 cách sử dụng
- Validation đảm bảo chỉ dùng 1 trong 2

---

## 📋 CÁCH SỬ DỤNG MỚI

### **✅ DEPOSIT API:**

#### **Sử dụng Account ID:**
```json
POST /api/transactions/deposit
{
  "accountId": 1,
  "amount": 1000000.00,
  "description": "Nạp tiền từ ATM",
  "createdBy": 1
}
```

#### **Sử dụng Account Number:**
```json
POST /api/transactions/deposit
{
  "accountNumber": "1234567890",
  "amount": 1000000.00,
  "description": "Nạp tiền từ ATM",
  "createdBy": 1
}
```

### **✅ WITHDRAWAL API:**

#### **Sử dụng Account ID:**
```json
POST /api/transactions/withdraw
{
  "accountId": 1,
  "amount": 500000.00,
  "description": "Rút tiền mặt",
  "createdBy": 1
}
```

#### **Sử dụng Account Number:**
```json
POST /api/transactions/withdraw
{
  "accountNumber": "1234567890",
  "amount": 500000.00,
  "description": "Rút tiền mặt",
  "createdBy": 1
}
```

### **✅ TRANSFER API:**

#### **Sử dụng Account ID:**
```json
POST /api/transactions/transfer
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 200000.00,
  "description": "Chuyển tiền cho bạn",
  "createdBy": 1
}
```

#### **Sử dụng Account Number:**
```json
POST /api/transactions/transfer
{
  "fromAccountNumber": "1234567890",
  "toAccountNumber": "0987654321",
  "amount": 200000.00,
  "description": "Chuyển tiền cho bạn",
  "createdBy": 1
}
```

#### **Kết hợp cả hai:**
```json
POST /api/transactions/transfer
{
  "fromAccountId": 1,
  "toAccountNumber": "0987654321",
  "amount": 200000.00,
  "description": "Chuyển tiền cho bạn",
  "createdBy": 1
}
```

---

## 🛡️ VALIDATION RULES

### **✅ Hợp lệ:**
- Chỉ cung cấp `accountId` (không cung cấp `accountNumber`)
- Chỉ cung cấp `accountNumber` (không cung cấp `accountId`)
- Kết hợp `fromAccountId` + `toAccountNumber` (hoặc ngược lại)

### **❌ Không hợp lệ:**
- Cung cấp cả `accountId` và `accountNumber` cho cùng 1 account
- Không cung cấp cả `accountId` và `accountNumber`
- Cung cấp `accountNumber` rỗng hoặc null

---

## 🔧 IMPLEMENTATION DETAILS

### **AccountResolver Utility:**
```java
@Component
public class AccountResolver {
    public Account resolveAccount(Integer accountId, String accountNumber) {
        if (accountId != null) {
            return accountRepository.findById(accountId);
        } else if (accountNumber != null) {
            return accountRepository.findByAccountNumber(accountNumber);
        } else {
            throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
        }
    }
}
```

### **Validation Logic:**
```java
public void validateAccountIdentifier(Integer accountId, String accountNumber) {
    if (accountId == null && (accountNumber == null || accountNumber.trim().isEmpty())) {
        throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
    }
    if (accountId != null && accountNumber != null && !accountNumber.trim().isEmpty()) {
        throw new IllegalArgumentException("Provide either accountId or accountNumber, not both");
    }
}
```

---

## 📊 PERFORMANCE COMPARISON

| Method | Performance | Use Case |
|--------|-------------|----------|
| **accountId** | ⚡ Fastest | Internal systems, APIs |
| **accountNumber** | 🐌 Slower | User interfaces, reports |

---

## 🎯 KHUYẾN NGHỊ SỬ DỤNG

### **🔒 Internal APIs:**
- Sử dụng `accountId` cho performance tốt nhất
- An toàn hơn, không thể đoán được

### **👥 User-facing APIs:**
- Sử dụng `accountNumber` cho user experience
- Dễ nhớ và sử dụng hơn

### **🔄 Mixed Scenarios:**
- Hỗ trợ cả hai để linh hoạt
- Validation đảm bảo tính nhất quán

---

## 🚀 LỢI ÍCH

### **✅ Flexibility:**
- Hỗ trợ nhiều cách sử dụng
- Dễ dàng tích hợp với các hệ thống khác

### **✅ Performance:**
- Tối ưu cho từng use case
- Caching hiệu quả hơn

### **✅ Security:**
- Bảo mật cao với accountId
- User-friendly với accountNumber

### **✅ Maintainability:**
- Code sạch và dễ hiểu
- Validation tập trung

---

## 🎉 KẾT LUẬN

**Bây giờ Transaction APIs hỗ trợ cả accountId và accountNumber!**

- ✅ **Linh hoạt**: Sử dụng theo nhu cầu
- ✅ **An toàn**: Validation đầy đủ
- ✅ **Hiệu quả**: Performance tối ưu
- ✅ **Dễ sử dụng**: User-friendly

**Sẵn sàng cho production!** 🚀
