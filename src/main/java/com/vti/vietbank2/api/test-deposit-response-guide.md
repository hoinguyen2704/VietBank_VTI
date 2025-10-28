# Test Deposit Response - Đã sửa lỗi

## Vấn đề đã được sửa

### ✅ **Related Account Fields cho Deposit/Withdrawal**
- **Logic đúng**: Khi nạp tiền và rút tiền, các trường `relatedAccount*` sẽ là null
- **Lý do**: Không có tài khoản liên quan trong giao dịch nạp/rút tiền
- **Chỉ chuyển khoản**: Mới có tài khoản liên quan

### ✅ **TransactionResponse đã được cải thiện**
1. Xử lý đúng việc kiểm tra `related_account_id != null`
2. Chỉ hiển thị thông tin tài khoản liên quan khi có
3. Deposit/Withdrawal sẽ có `relatedAccount*` = null

## Test API Deposit Response

### 1. Test deposit API
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d @test-deposit-response.json
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
    "relatedAccountId": null,
    "relatedAccountNumber": null,
    "relatedAccountName": null,
    "relatedCustomerName": null,
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

- **Deposit/Withdrawal**: `relatedAccount*` sẽ là null
- **Transfer**: `relatedAccount*` sẽ có giá trị
- Logic đã xử lý đúng việc kiểm tra null
- Response rõ ràng và dễ hiểu
