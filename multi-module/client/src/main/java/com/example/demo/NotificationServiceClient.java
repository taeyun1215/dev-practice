package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationService", url = "${notification.service.url}")
public interface NotificationServiceClient {

	@PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
	NotificationResponse sendNotification(@RequestBody NotificationRequest request);
}