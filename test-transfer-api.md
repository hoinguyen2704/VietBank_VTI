# Test Transfer API

## Request đã sửa lỗi

```json
{
  "fromAccountId": 1,
  "fromAccountNumber": "1000000000001",
  "toAccountId": 4,
  "toAccountNumber": "1000000000004",
  "amount": 2000,
  "description": "ck",
  "createdBy": 1
}
```

## Các lỗi đã được sửa:

1. **AccountResolver validation**: Cho phép cung cấp cả `accountId` và `accountNumber` cùng lúc
2. **Transaction code length**: Giảm độ dài transaction code để phù hợp với VARCHAR(20) trong database
3. **Transaction linking**: Đảm bảo `related_transaction_id` được set đúng

## Cách test:

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d @test-transfer.json
```

## Expected Response:

```json
{
  "success": true,
  "message": "Transfer completed successfully",
  "data": {
    "id": 123,
    "transactionCode": "TXN1234567890ABCDEF",
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
    "createdByName": "0900000001",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```
