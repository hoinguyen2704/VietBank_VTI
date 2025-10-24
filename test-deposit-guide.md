# Test Deposit API - Đã sửa lỗi

## Vấn đề đã được sửa

### ✅ **Thay đổi từ accountId sang accountNumber**
- **Thay đổi**: Loại bỏ `accountId`, chỉ cần `accountNumber`
- **Lợi ích**: Dễ sử dụng hơn, không cần biết account ID
- **Tự động**: Tìm account ID từ account number

### ✅ **DepositRequest đã được cải thiện**
1. Loại bỏ `accountId`
2. Chỉ cần `accountNumber` (bắt buộc)
3. Tự động tìm account ID từ account number

### ✅ **TransactionServiceImpl đã được cải thiện**
1. Sử dụng `accountNumber` để tìm account
2. Tự động tìm account ID từ account number
3. Validation và error handling được cải thiện

## Test API Deposit

### 1. Test deposit API
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d @test-deposit.json
```

## Request mẫu

```json
{
  "accountNumber": "1000000000001",
  "amount": 1000000,
  "description": "Nạp tiền vào tài khoản",
  "createdBy": 1
}
```

## Expected Response

```json
{
  "success": true,
  "message": "Deposit completed successfully",
  "data": {
    "id": 123,
    "transactionCode": "TXN1234567890ABCD",
    "transactionType": "DEPOSIT",
    "accountId": 1,
    "accountNumber": "1000000000001",
    "accountName": "NGUYEN VAN A",
    "customerName": "Nguyễn Văn A",
    "amount": 1000000,
    "fee": 0,
    "balanceAfter": 2500000,
    "description": "Nạp tiền vào tài khoản",
    "createdByName": "0900000003",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

## Lưu ý

- Đảm bảo account number tồn tại trong hệ thống
- Đảm bảo account có trạng thái ACTIVE
- Đảm bảo staff ID tồn tại
- Amount phải lớn hơn hoặc bằng 1000 VND
- Account number phải có 10-20 chữ số
- Account ID sẽ được tự động tìm từ account number
