package com.example.demo.global;

import lombok.RequiredArgsConstructor;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackNotificationAspect {

    private final SlackApi slackApi;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Around("@annotation(com.example.demo.global.annotation.SlackNotification)")
    public Object slackNotification(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest originalRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper request = originalRequest instanceof ContentCachingRequestWrapper
                ? (ContentCachingRequestWrapper) originalRequest
                : new ContentCachingRequestWrapper(originalRequest);
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setFallback("Error");
        slackAttachment.setColor("danger");
        slackAttachment.setTitle("Error Detected");
        slackAttachment.setTitleLink(request.getContextPath());
        slackAttachment.setFields(Arrays.asList(
                new SlackField().setTitle("Request URL").setValue(request.getRequestURL().toString()),
                new SlackField().setTitle("Request Method").setValue(request.getMethod()),
                new SlackField().setTitle("Request Time").setValue(new Date().toString()),
                new SlackField().setTitle("Headers").setValue("Authorization: " + request.getHeader("Authorization")),
                new SlackField().setTitle("Request Body").setValue(requestBody),
                new SlackField().setTitle("Request IP").setValue(request.getRemoteAddr()),
                new SlackField().setTitle("Request User-Agent").setValue(request.getHeader("User-Agent"))
        ));

        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setAttachments(Collections.singletonList(slackAttachment));
        slackMessage.setIcon(":ghost:");
        slackMessage.setText("Error Detected");
        slackMessage.setUsername("DutyPark");

        threadPoolTaskExecutor.execute(() -> slackApi.call(slackMessage));
        // slackApi.call(slackMessage);

        return proceedingJoinPoint.proceed();
    }
}