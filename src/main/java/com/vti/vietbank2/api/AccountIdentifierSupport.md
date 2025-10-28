# 🔄 HỖ TRỢ ACCOUNT IDENTIFIER TRONG VIETBANK2

## 📋 Tổng quan

VietBank2 hiện tại hỗ trợ việc xác định tài khoản thông qua **Account Number** trong các API giao dịch. Hệ thống đã có sẵn `AccountResolver` utility để hỗ trợ cả **Account ID** và **Account Number**, nhưng chưa được triển khai đầy đủ trong các Request DTOs.

## 🎯 Tình trạng hiện tại

### ✅ **Đã hỗ trợ:**
- **Account Number**: Tất cả API giao dịch đều sử dụng `accountNumber`
- **AccountResolver**: Utility class đã được tạo để hỗ trợ cả hai loại identifier
- **Validation**: Có validation cho account number

### ⚠️ **Chưa hỗ trợ:**
- **Account ID**: Chưa được tích hợp vào Request DTOs
- **Mixed identifiers**: Chưa hỗ trợ kết hợp cả hai loại

## 🏗️ Kiến trúc hiện tại

### **Request DTOs hiện tại:**

#### **DepositRequest:**
```java
@Data
public class DepositRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;  // ✅ Chỉ hỗ trợ accountNumber
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum deposit amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy;
}
```

#### **WithdrawalRequest:**
```java
@Data
public class WithdrawalRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;  // ✅ Chỉ hỗ trợ accountNumber
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum withdrawal amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy;
}
```

#### **TransferRequest:**
```java
@Data
public class TransferRequest {
    @NotBlank(message = "From account number is required")
    private String fromAccountNumber;  // ✅ Chỉ hỗ trợ accountNumber
    
    @NotBlank(message = "To account number is required")
    private String toAccountNumber;     // ✅ Chỉ hỗ trợ accountNumber
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum transfer amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy;
}
```

### **AccountResolver Utility:**
```java
@Component
@RequiredArgsConstructor
public class AccountResolver {
    
    private final AccountRepository accountRepository;
    
    public Account resolveAccount(Integer accountId, String accountNumber) {
        if (accountId != null) {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
        } else if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            return accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountNumber));
        } else {
            throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
        }
    }
    
    public void validateAccountIdentifier(Integer accountId, String accountNumber) {
        if (accountId == null && (accountNumber == null || accountNumber.trim().isEmpty())) {
            throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
        }
        // Cho phép cung cấp cả hai, sẽ ưu tiên accountId
    }
}
```

## 🚀 Đề xuất cải tiến

### **1. Cập nhật Request DTOs để hỗ trợ cả Account ID và Account Number:**

#### **DepositRequest cải tiến:**
```java
@Data
public class DepositRequest {
    // Account identifier - chỉ cần một trong hai
    private Integer accountId;           // Optional: Internal ID
    private String accountNumber;        // Optional: User-friendly number
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum deposit amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy;
    
    // Custom validation
    @AssertTrue(message = "Either accountId or accountNumber must be provided")
    public boolean isValidAccountIdentifier() {
        return (accountId != null) || (accountNumber != null && !accountNumber.trim().isEmpty());
    }
}
```

#### **TransferRequest cải tiến:**
```java
@Data
public class TransferRequest {
    // From account - chỉ cần một trong hai
    private Integer fromAccountId;
    private String fromAccountNumber;
    
    // To account - chỉ cần một trong hai
    private Integer toAccountId;
    private String toAccountNumber;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum transfer amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy;
    
    // Custom validation
    @AssertTrue(message = "Either fromAccountId or fromAccountNumber must be provided")
    public boolean isValidFromAccountIdentifier() {
        return (fromAccountId != null) || (fromAccountNumber != null && !fromAccountNumber.trim().isEmpty());
    }
    
    @AssertTrue(message = "Either toAccountId or toAccountNumber must be provided")
    public boolean isValidToAccountIdentifier() {
        return (toAccountId != null) || (toAccountNumber != null && !toAccountNumber.trim().isEmpty());
    }
}
```

### **2. Cập nhật Service Implementation:**

#### **TransactionServiceImpl cải tiến:**
```java
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountResolver accountResolver;
    // ... other dependencies

    @Override
    @Transactional
    public ApiResponse<TransactionResponse> deposit(DepositRequest request) {
        // Sử dụng AccountResolver để resolve account
        Account account = accountResolver.resolveAccount(request.getAccountId(), request.getAccountNumber());
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }

        // ... rest of the implementation
    }

    @Override
    @Transactional
    public ApiResponse<TransactionResponse> transfer(TransferRequest request) {
        // Resolve both accounts using AccountResolver
        Account fromAccount = accountResolver.resolveAccount(request.getFromAccountId(), request.getFromAccountNumber());
        Account toAccount = accountResolver.resolveAccount(request.getToAccountId(), request.getToAccountNumber());
        
        // ... rest of the implementation
    }
}
```

## 📊 So sánh Performance

| Method | Performance | Use Case | Security |
|--------|-------------|----------|----------|
| **accountId** | ⚡ Fastest (Primary Key) | Internal APIs, Batch processing | 🔒 High (Non-guessable) |
| **accountNumber** | 🐌 Slower (Index lookup) | User interfaces, Reports | ⚠️ Medium (User-friendly) |

## 🎯 Lợi ích của việc hỗ trợ cả hai

### **🔒 Bảo mật:**
- **accountId**: Internal ID, không thể đoán được, an toàn hơn
- **accountNumber**: Có thể bị brute force, nhưng user-friendly

### **🚀 Performance:**
- **accountId**: PRIMARY KEY, index nhanh nhất
- **accountNumber**: Cần tìm kiếm qua bảng accounts

### **👥 User Experience:**
- **accountId**: Dành cho internal systems
- **accountNumber**: Dành cho end users (dễ nhớ hơn)

### **🛡️ Flexibility:**
- Hỗ trợ cả 2 cách sử dụng
- Validation đảm bảo chỉ dùng 1 trong 2

## 📝 Ví dụ sử dụng sau khi cải tiến

### **Nạp tiền bằng Account ID:**
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{
  "accountId": 1,
    "amount": 1000000,
  "description": "Nạp tiền từ ATM",
  "createdBy": 1
  }'
```

### **Nạp tiền bằng Account Number:**
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1000000000001",
    "amount": 1000000,
  "description": "Nạp tiền từ ATM",
  "createdBy": 1
  }'
```

### **Chuyển khoản kết hợp:**
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
  "fromAccountId": 1,
    "toAccountNumber": "1000000000002",
    "amount": 300000,
    "description": "Chuyển khoản",
  "createdBy": 1
  }'
```

## 🛡️ Validation Rules

### **✅ Hợp lệ:**
- Chỉ cung cấp `accountId` (không cung cấp `accountNumber`)
- Chỉ cung cấp `accountNumber` (không cung cấp `accountId`)
- Kết hợp `fromAccountId` + `toAccountNumber` (hoặc ngược lại)

### **❌ Không hợp lệ:**
- Không cung cấp cả `accountId` và `accountNumber`
- Cung cấp `accountNumber` rỗng hoặc null khi không có `accountId`

## 🎯 Khuyến nghị triển khai

### **Phase 1: Cập nhật Request DTOs**
1. Thêm `accountId` fields vào các Request DTOs
2. Thêm custom validation methods
3. Cập nhật validation messages

### **Phase 2: Cập nhật Service Layer**
1. Sử dụng `AccountResolver` trong TransactionService
2. Cập nhật error handling
3. Thêm unit tests

### **Phase 3: Testing & Documentation**
1. Test tất cả scenarios
2. Cập nhật API documentation
3. Performance testing

## 🚀 Kết luận

**VietBank2 đã có foundation tốt với AccountResolver utility!**

### **Hiện tại:**
- ✅ Hỗ trợ Account Number đầy đủ
- ✅ AccountResolver đã sẵn sàng
- ✅ Validation cơ bản đã có

### **Cần cải tiến:**
- 🔄 Thêm Account ID support vào Request DTOs
- 🔄 Tích hợp AccountResolver vào Service layer
- 🔄 Cập nhật validation rules

### **Lợi ích sau khi hoàn thiện:**
- ✅ **Linh hoạt**: Sử dụng theo nhu cầu
- ✅ **An toàn**: Validation đầy đủ
- ✅ **Hiệu quả**: Performance tối ưu
- ✅ **Dễ sử dụng**: User-friendly

**Sẵn sàng cho production sau khi hoàn thiện!** 🚀