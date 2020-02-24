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

    @Autowired TargetRepository targetRepository;
    @Value("${application.alertmanager.targetId}") String targetId;
    @Value("${application.phone.use}") boolean phoneUse;
    @Value("${application.email.use}") boolean emailUse;
    @Value("${application.slack.use}") boolean slackUse;
    @Value("${application.msteams.use}") boolean msteamsUse;
    @Autowired PhoneSender phoneSender;
    @Autowired EmailSender emailSender;
    @Autowired SlackSender slackSender;
    @Autowired MsteamsSender msteamsSender;
    @Autowired WebhookSender webhookSender;

    // TODO partial success? result가 1개라도 fail이면 fail?
    public
    NotifyResponse notify(NotifyRequest request) {
        NotifyResponse response = new NotifyResponse();
        boolean result = true;

        // Lookup notification
        Optional<Target> target = targetRepository.findById(request.getTargetId());
        if (!target.isPresent())
            return makeErrorResponse(request, String.format("Target id(%s) not found", request.getTargetId()));

        request = mergeTarget(request, target);
        logger.debug("merged request={}", request);
        if (existPhone(request) && phoneUse)
            response.setPhone(phoneSender.notify(request));
        if (existEmail(request) && emailUse)
            response.setEmail(emailSender.notify(request));
        if (existSlack(request) && slackUse)
            response.setSlack(slackSender.notify(request));
        if (existMsteams(request) && msteamsUse)
            response.setMsteams(msteamsSender.notify(request));
        response.setWebhook(webhookSender.notify(request));

        response.setId(request.getId());
        response.setResult(result);
        return response;
    }

    private NotifyRequest mergeTarget(NotifyRequest request, Optional<Target> target) {
        request.getPhone().addAll(target.get().getPhoneList());
        request.getEmail().addAll(target.get().getEmailList());
        request.getSlack().addAll(target.get().getSlackList());
        request.getMsteams().addAll(target.get().getMsteamsList());
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
    String webhookTitle = "[Notification - AlertManager]";
    String webhookMessage = "Alert: {{ .commonLabels.alertname }}\nSummary:{{ .commonAnnotations.summary }}";   // + "RawData: {{ .CommonLabels }}";
    public NotifyResponse notifyWebhook(Map webhook) {
        NotifyRequest request = new NotifyRequest();
        request.setId(String.format("alertmanager-%d", webhookId++));
        request.setTargetId(buildTargetId(webhook, targetId));
        request.setTitle(webhookTitle);
        request.setMessage(buildWebhookMessage(webhook));
        return notify(request);
    }

    private String buildTargetId(Map webhook, String targetId) {
        return JsonHelper.getExpressionValue(webhook, targetId, "json");
    }

    private String buildWebhookMessage(Map webhook) {
        String status = (String) webhook.get("status");
        List list = (List) webhook.get("alerts");
        int count = list.size();
        String message = JsonHelper.getExpressionValue(webhook, webhookMessage, "json");
        return String.format("[%s:%d]\n%s", status, count, message);
/*
        String status = (String) webhook.get("status");
        List list = (List) webhook.get("alerts");
        int count = list.size();

        Map map = (Map) webhook.get("commonLabels");
        String alertname = (String) map.get("alertname");
        map = (Map) webhook.get("commonAnnotations");
        String summary = (String) map.getOrDefault("summary", "");
        return String.format("[%s:%d]\nAlert:%s\nSummary:%s", status, count, alertname, summary);
 */
    }

    private boolean existPhone(NotifyRequest request) {
        if (request.getPhone() != null && request.getPhone().size() > 0)
            return true;
        return false;
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

    private boolean existMsteams(NotifyRequest request) {
        if (request.getMsteams() != null && request.getMsteams().size() > 0)
            return true;
        return false;
    }


}
