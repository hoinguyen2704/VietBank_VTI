# Test Transfer API - Đã sửa lỗi

## Vấn đề đã được sửa

### ✅ **Lỗi "Duplicate entry for key 'transaction_history.transaction_code'"**
- **Nguyên nhân**: Transaction code bị trùng lặp do sử dụng timestamp đầy đủ
- **Giải pháp**: Sử dụng timestamp ngắn hơn + UUID để đảm bảo tính duy nhất

### ✅ **TransactionServiceImpl đã được cải thiện**
1. Sửa method `generateTransactionCode()` để tạo code duy nhất
2. Sử dụng timestamp ngắn hơn + UUID
3. Đảm bảo không có transaction code trùng lặp

## Test API Transfer

### 1. Test transfer API
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d @test-transfer.json
```

## Request mẫu

```json
{
  "fromAccountNumber": "1000000000001",
  "toAccountNumber": "1000000000004",
  "amount": 2000,
  "description": "ck",
  "createdBy": 1
}
```

## Expected Response

```json
{
  "success": true,
  "message": "Transfer completed successfully",
  "data": {
    "id": 123,
    "transactionCode": "TXN1234567890ABCD",
    "transactionType": "TRANSFER_OUT",
    "accountId": 1,
    "accountNumber": "1000000000001",
    "accountName": "NGUYEN VAN A",
    "customerName": "Nguyễn Văn A",
    "relatedAccountId": 4,
    "relatedAccountNumber": "1000000000004",
    "relatedAccountName": "TRAN THI B",
    "relatedCustomerName": "Trần Thị B",
    "amount": 2000,
    "fee": 0,
    "balanceAfter": 998000,
    "description": "ck",
    "createdByName": "0900000003",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

## Lưu ý

- Đảm bảo fromAccountNumber và toAccountNumber tồn tại trong hệ thống
- Đảm bảo cả hai account đều có trạng thái ACTIVE
- Đảm bảo staff ID tồn tại
- Amount phải lớn hơn hoặc bằng 1000 VND
- Account numbers phải có 10-20 chữ số
- Transaction code sẽ được tạo duy nhất