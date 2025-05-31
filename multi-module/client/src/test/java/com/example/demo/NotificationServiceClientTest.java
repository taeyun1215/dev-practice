package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationServiceClientTest extends ClientContextTest {

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @Test
    @DisplayName("외부 시스템 sendNotification 통합 테스트")
    void sendNotificationTest() {
        NotificationRequest request = new NotificationRequest("user@example.com", "Your order has been shipped!");
        NotificationResponse response = notificationServiceClient.sendNotification(request);
        assertNotNull(response);
        assertEquals("Success", response.notificationStatus());
    }
}
