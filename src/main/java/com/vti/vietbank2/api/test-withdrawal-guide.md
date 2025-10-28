# Test Withdrawal API - Đã sửa lỗi

## Vấn đề đã được sửa

### ✅ **Lỗi "Column 'balance_after' cannot be null"**
- **Nguyên nhân**: Transaction được lưu với `balance_after` là null trước khi cập nhật balance
- **Giải pháp**: Tính toán `balance_after` trước khi lưu transaction

### ✅ **TransactionServiceImpl đã được cải thiện**
1. Tính toán `newBalance` trước khi tạo transaction
2. Set `balance_after` ngay khi tạo transaction
3. Cập nhật account balance
4. Không còn lỗi null constraint

## Test API Withdrawal

### 1. Test withdrawal API
```bash
curl -X POST http://localhost:8080/api/transactions/withdraw \
  -H "Content-Type: application/json" \
  -d @test-withdrawal.json
```

## Request mẫu

```json
{
  "accountNumber": "1000000000001",
  "amount": 500000,
  "description": "Rút tiền từ tài khoản",
  "createdBy": 1
}
```

## Expected Response

```json
{
  "success": true,
  "message": "Withdrawal completed successfully",
  "data": {
    "id": 123,
    "transactionCode": "TXN1234567890ABCD",
    "transactionType": "WITHDRAWAL",
    "accountId": 1,
    "accountNumber": "1000000000001",
    "accountName": "NGUYEN VAN A",
    "customerName": "Nguyễn Văn A",
    "relatedAccountId": null,
    "relatedAccountNumber": null,
    "relatedAccountName": null,
    "relatedCustomerName": null,
    "amount": 500000,
    "fee": 0,
    "balanceBefore": 2500000,
    "balanceAfter": 2000000,
    "description": "Rút tiền từ tài khoản",
    "createdByName": "0900000003",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

## Lưu ý

- Đảm bảo account number tồn tại trong hệ thống
- Đảm bảo account có trạng thái ACTIVE
- Đảm bảo account có đủ số dư để rút tiền
- Đảm bảo staff ID tồn tại
- Amount phải lớn hơn hoặc bằng 1000 VND
- Account number phải có 10-20 chữ số
- Balance_after được tính toán đúng trước khi lưu transaction