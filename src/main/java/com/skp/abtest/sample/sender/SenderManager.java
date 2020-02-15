package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.entity.*;
import com.skp.abtest.sample.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SenderManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired SlackSender slackSender;
    @Autowired EmailSender emailSender;
    @Autowired WebhookSender webhookSender;
    @Value("${application.alertmanager.notificationId}") String notificationId;

    // TODO partial success? result가 1개라도 fail이면 fail?
    public
    NotifyResponse notify(NotifyRequest request) {
        NotifyResponse response = new NotifyResponse();
        boolean result = true;

        // Lookup notification
        Optional<Notification> notification = notificationRepository.findById(request.getNotificationId());
        if (!notification.isPresent())
            return makeErrorResponse(request, "Notification id not found");

        request = mergeTarget(request, notification);
        logger.debug("merged request={}", request);
        if (existSlack(request))
            response.setSlack(slackSender.notify(request));
        if (existEmail(request))
            response.setEmail(emailSender.notify(request));

        response.setWebhook(webhookSender.notify(request));

        response.setId(request.getId());
        response.setResult(result);
        return response;
    }

    private NotifyRequest mergeTarget(NotifyRequest request, Optional<Notification> notification) {
        request.getEmail().addAll(notification.get().getEmailList());
        request.getSlack().addAll(notification.get().getSlackList());
        request.getPhones().addAll(notification.get().getPhoneList());
        return request;
    }

    private NotifyResponse makeErrorResponse(NotifyRequest request, String error) {
        NotifyResponse response = new NotifyResponse();
        response.setId(request.getId());
        response.setResult(false);
        response.setError(error);
        response.setStatus(400);
        return response;
    }

    long webhookId = 1;
    public NotifyResponse notifyWebhook(Map webhook) {
        NotifyRequest request = new NotifyRequest();
        request.setId(String.format("alertmanager-%d", webhookId++));
        request.setNotificationId(buildNotificationId(webhook, notificationId));
        request.setTitle("[Notification - AlertManager]");
        request.setMessage(buildWebhookMessage(webhook));
        return notify(request);
    }

    private String buildNotificationId(Map webhook, String notificationId) {
        return JsonHelper.getExpValue(webhook, notificationId);
    }

    // 'Alert: {{ .CommonLabels.alertname }}. Summary:{{ .CommonAnnotations.summary }}. RawData: {{ .CommonLabels }}'
    private String buildWebhookMessage(Map webhook) {
        String status = (String) webhook.get("status");
        List list = (List) webhook.get("alerts");
        int count = list.size();

        Map map = (Map) webhook.get("commonLabels");
        String alertname = (String) map.get("alertname");
        map = (Map) webhook.get("commonAnnotations");
        String summary = (String) map.getOrDefault("summary", "");
        return String.format("[%s:%d]\nAlert:%s\nSummary:%s", status, count, alertname, summary);
    }

    private boolean existEmail(NotifyRequest request) {
        if (request.getEmail() != null && request.getEmail().size() > 0)
            return true;
        return false;
    }

    private boolean existSlack(NotifyRequest request) {
        if (request.getSlack() != null && request.getSlack().size() > 0)
            return true;
        return false;
    }

    private boolean existPhone(NotifyRequest request) {
        if (request.getPhones() != null && request.getPhones().size() > 0)
            return true;
        return false;
    }

}
