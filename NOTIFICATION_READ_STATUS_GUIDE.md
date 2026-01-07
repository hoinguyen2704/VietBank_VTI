# Hướng Dẫn Quản Lý Trạng Thái Đọc Thông Báo

## Tổng Quan

Hệ thống sử dụng field `isRead` (Boolean) trong database để theo dõi thông báo đã đọc hay chưa.

## Database Schema

```sql
CREATE TABLE notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,  -- ← Field này
    related_transaction_id INT NULL,
    related_account_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ...
);
```

## API Endpoints

### 1. Lấy TẤT CẢ thông báo (có cả đã đọc và chưa đọc)
```http
GET /api/notifications
Authorization: Bearer {token}
```

**Response:**
```json
{
    "success": true,
    "data": [
        {
            "id": 1,
            "title": "Chuyển tiền thành công",
            "message": "Bạn đã chuyển 1.000.000 ₫...",
            "type": "TRANSFER_SENT",
            "isRead": false,  // ← false = chưa đọc
            "createdAt": "2024-01-15T10:30:00"
        },
        {
            "id": 2,
            "title": "Nhận tiền thành công",
            "message": "Bạn đã nhận 500.000 ₫...",
            "type": "TRANSFER_RECEIVED",
            "isRead": true,   // ← true = đã đọc
            "createdAt": "2024-01-15T09:20:00"
        }
    ]
}
```

### 2. Lấy CHỈ thông báo CHƯA ĐỌC
```http
GET /api/notifications/unread
Authorization: Bearer {token}
```

**Response:** Chỉ trả về các thông báo có `isRead: false`

### 3. Lấy CHỈ thông báo ĐÃ ĐỌC
```http
GET /api/notifications/read
Authorization: Bearer {token}
```

**Response:** Chỉ trả về các thông báo có `isRead: true`

### 4. Đếm số thông báo CHƯA ĐỌC
```http
GET /api/notifications/unread/count
Authorization: Bearer {token}
```

**Response:**
```json
{
    "success": true,
    "data": 5  // Có 5 thông báo chưa đọc
}
```

### 5. Đánh dấu 1 thông báo ĐÃ ĐỌC
```http
PUT /api/notifications/{id}/read
Authorization: Bearer {token}
```

**Ví dụ:**
```http
PUT /api/notifications/1/read
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
{
    "success": true,
    "message": "Notification marked as read"
}
```

### 6. Đánh dấu TẤT CẢ thông báo ĐÃ ĐỌC
```http
PUT /api/notifications/read-all
Authorization: Bearer {token}
```

## Cách Sử Dụng từ Client

### JavaScript/TypeScript Example

```javascript
// 1. Lấy tất cả thông báo và phân loại
async function getAllNotifications() {
    const response = await fetch('/api/notifications', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    const result = await response.json();
    
    const notifications = result.data;
    const unread = notifications.filter(n => !n.isRead);
    const read = notifications.filter(n => n.isRead);
    
    console.log(`Có ${unread.length} thông báo chưa đọc`);
    console.log(`Có ${read.length} thông báo đã đọc`);
    
    return { unread, read };
}

// 2. Lấy chỉ thông báo chưa đọc
async function getUnreadNotifications() {
    const response = await fetch('/api/notifications/unread', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    const result = await response.json();
    return result.data; // Chỉ có thông báo isRead: false
}

// 3. Đếm số thông báo chưa đọc (cho badge)
async function getUnreadCount() {
    const response = await fetch('/api/notifications/unread/count', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    const result = await response.json();
    return result.data; // Số lượng (number)
}

// 4. Đánh dấu đã đọc khi user click vào thông báo
async function markAsRead(notificationId) {
    const response = await fetch(`/api/notifications/${notificationId}/read`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    return response.json();
}

// 5. Đánh dấu tất cả đã đọc
async function markAllAsRead() {
    const response = await fetch('/api/notifications/read-all', {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    return response.json();
}
```

### React Hook Example

```typescript
import { useState, useEffect } from 'react';

export const useNotifications = (token: string) => {
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const [loading, setLoading] = useState(true);

    // Lấy tất cả thông báo
    useEffect(() => {
        const fetchNotifications = async () => {
            try {
                const response = await fetch('/api/notifications', {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                const result = await response.json();
                setNotifications(result.data);
                
                // Đếm số chưa đọc
                const unread = result.data.filter((n: any) => !n.isRead);
                setUnreadCount(unread.length);
            } catch (error) {
                console.error('Error fetching notifications:', error);
            } finally {
                setLoading(false);
            }
        };
        
        fetchNotifications();
        // Refresh mỗi 30 giây
        const interval = setInterval(fetchNotifications, 30000);
        return () => clearInterval(interval);
    }, [token]);

    // Đánh dấu đã đọc
    const markAsRead = async (id: number) => {
        try {
            await fetch(`/api/notifications/${id}/read`, {
                method: 'PUT',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            // Update local state
            setNotifications(prev => 
                prev.map(n => n.id === id ? { ...n, isRead: true } : n)
            );
            setUnreadCount(prev => Math.max(0, prev - 1));
        } catch (error) {
            console.error('Error marking as read:', error);
        }
    };

    return {
        notifications,
        unreadCount,
        loading,
        markAsRead
    };
};
```

### Vue.js Example

```vue
<template>
  <div>
    <!-- Badge hiển thị số thông báo chưa đọc -->
    <div class="notification-badge" v-if="unreadCount > 0">
      {{ unreadCount }}
    </div>

    <!-- Danh sách thông báo -->
    <div v-for="notification in notifications" :key="notification.id">
      <div 
        :class="{ 'unread': !notification.isRead }"
        @click="markAsRead(notification.id)"
      >
        <h3>{{ notification.title }}</h3>
        <p>{{ notification.message }}</p>
        <span v-if="!notification.isRead" class="unread-dot"></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const notifications = ref([]);
const unreadCount = ref(0);

const fetchNotifications = async () => {
  const response = await fetch('/api/notifications', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  const result = await response.json();
  notifications.value = result.data;
  unreadCount.value = result.data.filter(n => !n.isRead).length;
};

const markAsRead = async (id) => {
  await fetch(`/api/notifications/${id}/read`, {
    method: 'PUT',
    headers: { 'Authorization': `Bearer ${token}` }
  });
  // Update local state
  const index = notifications.value.findIndex(n => n.id === id);
  if (index !== -1) {
    notifications.value[index].isRead = true;
    unreadCount.value--;
  }
};

onMounted(() => {
  fetchNotifications();
});
</script>

<style>
.unread {
  background-color: #e3f2fd;
  font-weight: bold;
}
.unread-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: #2196F3;
  border-radius: 50%;
  margin-left: 8px;
}
</style>
```

## Luồng Hoạt Động

### Khi User Nhận Thông Báo Mới (WebSocket)

1. Server gửi thông báo qua WebSocket với `isRead: false`
2. Client hiển thị thông báo với badge "chưa đọc"
3. Khi user click vào thông báo:
   - Client gọi `PUT /api/notifications/{id}/read`
   - Server cập nhật `is_read = true` trong database
   - Client cập nhật UI (bỏ badge, đổi màu)

### Khi User Mở App

1. Client gọi `GET /api/notifications/unread/count` để hiển thị badge
2. Client gọi `GET /api/notifications` để hiển thị danh sách
3. Phân loại UI: tab "Chưa đọc" và "Đã đọc"

## Best Practices

### 1. Auto-mark as read khi user xem
```javascript
// Khi user scroll đến thông báo hoặc mở detail
function onNotificationView(notificationId) {
    if (!notification.isRead) {
        markAsRead(notificationId);
    }
}
```

### 2. Refresh unread count định kỳ
```javascript
// Refresh mỗi 30 giây
setInterval(async () => {
    const count = await getUnreadCount();
    updateBadge(count);
}, 30000);
```

### 3. Mark all as read khi user vào trang notifications
```javascript
// Khi user vào trang xem tất cả thông báo
function onNotificationsPageOpen() {
    markAllAsRead(); // Đánh dấu tất cả đã đọc
}
```

## Kiểm Tra Trạng Thái

### Trong Database
```sql
-- Xem tất cả thông báo của user
SELECT id, title, is_read, created_at 
FROM notifications 
WHERE user_id = 1 
ORDER BY created_at DESC;

-- Đếm số chưa đọc
SELECT COUNT(*) 
FROM notifications 
WHERE user_id = 1 AND is_read = FALSE;

-- Xem chỉ thông báo chưa đọc
SELECT * 
FROM notifications 
WHERE user_id = 1 AND is_read = FALSE;
```

## Tóm Tắt

| Trạng thái | `isRead` | API Endpoint |
|------------|----------|--------------|
| Chưa đọc | `false` | `GET /api/notifications/unread` |
| Đã đọc | `true` | `GET /api/notifications/read` |
| Tất cả | Cả hai | `GET /api/notifications` |
| Đếm chưa đọc | - | `GET /api/notifications/unread/count` |
| Đánh dấu đã đọc | - | `PUT /api/notifications/{id}/read` |
| Đánh dấu tất cả | - | `PUT /api/notifications/read-all` |

