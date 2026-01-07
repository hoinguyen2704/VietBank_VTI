# Hướng Dẫn Sử Dụng WebSocket Notification

## Tổng Quan

Hệ thống thông báo real-time sử dụng WebSocket để gửi thông báo ngay lập tức khi có giao dịch chuyển tiền thành công. Cả người gửi và người nhận đều nhận được thông báo real-time.

## Cấu Trúc

### Backend Components

1. **Notification Entity** - Lưu thông báo vào database
2. **NotificationService** - Xử lý logic gửi thông báo
3. **WebSocketConfig** - Cấu hình WebSocket và authentication
4. **NotificationController** - REST API để quản lý thông báo
5. **TransactionServiceImpl** - Tích hợp gửi thông báo khi chuyển tiền thành công

### Database

Chạy script SQL để tạo bảng notifications:
```sql
mysql -u root -p vietbank < database/add_notifications_table.sql
```

## Kết Nối WebSocket từ Client

### JavaScript/TypeScript (React, Vue, Angular)

```javascript
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

// Tạo kết nối WebSocket
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    connectHeaders: {
        Authorization: `Bearer ${yourJwtToken}` // JWT token từ login
    },
    onConnect: (frame) => {
        console.log('Connected: ' + frame);
        
        // Subscribe để nhận thông báo
        const phoneNumber = getCurrentUserPhoneNumber(); // Lấy số điện thoại từ token
        stompClient.subscribe(`/user/${phoneNumber}/queue/notifications`, (message) => {
            const notification = JSON.parse(message.body);
            console.log('Received notification:', notification);
            
            // Hiển thị thông báo cho user
            showNotification(notification);
        });
    },
    onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    }
});

// Kết nối
stompClient.activate();
```

### React Hook Example

```typescript
import { useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

export const useWebSocketNotifications = (token: string, phoneNumber: string) => {
    const stompClientRef = useRef<Client | null>(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            onConnect: () => {
                client.subscribe(`/user/${phoneNumber}/queue/notifications`, (message) => {
                    const notification = JSON.parse(message.body);
                    // Handle notification
                    console.log('New notification:', notification);
                });
            }
        });

        client.activate();
        stompClientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [token, phoneNumber]);

    return stompClientRef.current;
};
```

## REST API Endpoints

### Lấy tất cả thông báo
```
GET /api/notifications
Authorization: Bearer {token}
```

### Lấy thông báo chưa đọc
```
GET /api/notifications/unread
Authorization: Bearer {token}
```

### Đếm số thông báo chưa đọc
```
GET /api/notifications/unread/count
Authorization: Bearer {token}
```

### Đánh dấu đã đọc
```
PUT /api/notifications/{id}/read
Authorization: Bearer {token}
```

### Đánh dấu tất cả đã đọc
```
PUT /api/notifications/read-all
Authorization: Bearer {token}
```

## Luồng Hoạt Động

1. **Client kết nối WebSocket** với JWT token trong header
2. **Server xác thực token** và lưu user vào session
3. **Client subscribe** vào `/user/{phoneNumber}/queue/notifications`
4. **Khi chuyển tiền thành công**:
   - Server tạo 2 notification records (cho sender và receiver)
   - Server gửi thông báo real-time qua WebSocket
   - Cả 2 user nhận được thông báo ngay lập tức

## Format Thông Báo

```json
{
    "id": 1,
    "title": "Chuyển tiền thành công",
    "message": "Bạn đã chuyển 1.000.000 ₫ đến tài khoản 1000000000002. Mã giao dịch: TXN1234567890",
    "type": "TRANSFER_SENT",
    "isRead": false,
    "relatedTransactionId": 123,
    "relatedAccountId": 1,
    "createdAt": "2024-01-15T10:30:00"
}
```

## Cài Đặt Dependencies

### Frontend (npm/yarn)
```bash
npm install sockjs-client @stomp/stompjs
# hoặc
yarn add sockjs-client @stomp/stompjs
```

## Lưu Ý Bảo Mật

1. **Production**: Thay đổi `setAllowedOriginPatterns("*")` thành domain cụ thể
2. **HTTPS**: Sử dụng WSS (WebSocket Secure) trong production
3. **Token Validation**: WebSocket connection yêu cầu JWT token hợp lệ
4. **Rate Limiting**: Cân nhắc thêm rate limiting cho WebSocket connections

## Troubleshooting

### Không nhận được thông báo
1. Kiểm tra JWT token có hợp lệ không
2. Kiểm tra phone number trong subscription path có đúng không
3. Kiểm tra WebSocket connection có active không
4. Xem logs server để kiểm tra lỗi authentication

### Connection bị ngắt
- Client tự động reconnect mỗi 5 giây
- Kiểm tra network connection
- Kiểm tra server có đang chạy không

