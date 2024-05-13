package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NotificationServiceClientTest extends ClientContextTest {

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @Test
    public void testSendNotification() {
        NotificationRequest request = new NotificationRequest("user@example.com", "Your order has been shipped!");
        NotificationResponse response = notificationServiceClient.sendNotification(request);
        assertNotNull(response);
        assertEquals("Success", response.notificationStatus());
    }
}
