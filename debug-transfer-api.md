# Debug Transfer API - Lỗi 500

## Vấn đề
API transfer trả về lỗi 500 với request:
```json
{
  "fromAccountNumber": "1000000000001",
  "toAccountNumber": "1000000000004",
  "amount": 2000,
  "description": "ck",
  "createdBy": 1
}
```

## Nguyên nhân có thể

### 1. Staff ID không tồn tại
- **Vấn đề**: `createdBy: 1` có thể không tồn tại trong database
- **Giải pháp**: Kiểm tra staff ID có tồn tại không

### 2. Account không tồn tại
- **Vấn đề**: Account number có thể không tồn tại
- **Giải pháp**: Kiểm tra account number có tồn tại không

### 3. Transaction code quá dài
- **Vấn đề**: Transaction code có thể dài hơn 20 ký tự
- **Giải pháp**: Đã sửa method generateTransactionCode

## Cách debug

### 1. Kiểm tra staff ID có tồn tại không
```bash
curl -X GET http://localhost:8080/api/transactions/staff/1/exists
```

### 2. Kiểm tra account number có tồn tại không
```bash
curl -X GET http://localhost:8080/api/accounts/account-number/1000000000001
```

### 3. Kiểm tra account number đích có tồn tại không
```bash
curl -X GET http://localhost:8080/api/accounts/account-number/1000000000004
```

## Staff ID hợp lệ trong database

Theo database schema, các staff được tạo với ID tự động tăng. Hãy thử với staff ID khác:

```json
{
  "fromAccountNumber": "1000000000001",
  "toAccountNumber": "1000000000004",
  "amount": 2000,
  "description": "ck",
  "createdBy": 1
}
```

## Request đã sửa lỗi

Nếu staff ID 1 không tồn tại, hãy thử với staff ID khác hoặc kiểm tra database để tìm staff ID hợp lệ.

## Các lỗi đã được sửa

1. **AccountResolver validation**: Cho phép cung cấp cả accountId và accountNumber
2. **Transaction code length**: Giảm độ dài transaction code
3. **Transaction linking**: Đảm bảo related_transaction_id được set đúng
4. **Staff validation**: Thêm API để kiểm tra staff ID có tồn tại không
