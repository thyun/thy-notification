package com.thy.notification.sender;

import com.thy.notification.entity.NotifyRequest;
import com.thy.notification.entity.NotifyResponse;
import com.thy.notification.entity.Target;
import com.thy.notification.entity.TargetRepository;
import com.thy.notification.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SenderManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TargetRepository targetRepository;
    @Value("${application.alertmanager.targetKey}") String targetKey;
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
    public NotifyResponse notify(NotifyRequest request) {
        NotifyResponse response = new NotifyResponse();
        boolean result = true;

        // Lookup notification
        Optional<Target> target = lookupTarget(request.getTargetKey());

        mergeTarget(request, target);
        logger.debug("merged request={}", request);
        if (phoneUse)
            response.setPhone(phoneSender.notify(request));
        if (emailUse)
            response.setEmail(emailSender.notify(request));
        if (slackUse)
            response.setSlack(slackSender.notify(request));
        if (msteamsUse)
            response.setMsteams(msteamsSender.notify(request));
        response.setWebhook(webhookSender.notify(request));
//        webhookSender.notifyConfig(request);

        response.setId(request.getId());
        response.setResult(result);
        return response;
    }

    private Optional<Target> lookupTarget(String targetKey) {
        Optional<Target> target = targetRepository.findByKey(targetKey);
        if (target.isPresent())
            return target;
        logger.debug("Target key({}) not found. Use default", targetKey);
        return targetRepository.findByKey("default");
    }

    private void mergeTarget(NotifyRequest request, Optional<Target> target) {
        if (target.isPresent()) {
            request.getPhone().addAll(target.get().getPhoneList());
            request.getEmail().addAll(target.get().getEmailList());
            request.getSlack().addAll(target.get().getSlackList());
            request.getMsteams().addAll(target.get().getMsteamsList());
            request.getWebhook().addAll(target.get().getWebhookList());
        }
    }

    private NotifyResponse makeErrorResponse(NotifyRequest request, String error) {
        NotifyResponse response = new NotifyResponse();
        response.setId(request.getId());
        response.setResult(false);
        response.setError(error);
        response.setStatus(400);
        return response;
    }

    long alertmanagerId = 1;
    String alertmanagerTitle = "[thy-notification - {{ .groupLabels.alertname }}]";
    String alertmanagerMessage = "[{{ .status }}] [Alert: {{ .labels.alertname }}] {{ .startsAt }} [Summary:{{ .annotations.summary }}] {{ .labels }}";
    public NotifyResponse notifyAlertmanager(Map alertmanagerWebhook) {
        NotifyRequest request = new NotifyRequest();
        request.setId(String.format("alertmanager-%d", alertmanagerId++));
        request.setTargetKey(buildTargetKey(targetKey, alertmanagerWebhook));
        request.setTitle(buildAlertmanagerTitle(alertmanagerTitle, alertmanagerWebhook));
        request.setMessage(buildAlertmanagerMessage(alertmanagerMessage, alertmanagerWebhook));
        return notify(request);
    }

    private String buildTargetKey(String template, Map alertmanagerWebhook) {
        return JsonHelper.getExpressionValue(alertmanagerWebhook, template, "json");
    }

    private String buildAlertmanagerTitle(String template, Map alertmanagerWebhook) {
        String value = JsonHelper.getExpressionValue(alertmanagerWebhook, template, "json");
        return String.format("%s", value);
    }

    private String buildAlertmanagerMessage(String template, Map alertmanagerWebhook) {
        ArrayList<Map> alerts = (ArrayList<Map>) alertmanagerWebhook.get("alerts");

        String result = alerts.stream()
                .map(element -> JsonHelper.getExpressionValue(element, template, "json"))
                .collect(Collectors.joining("   \n"));      // Markdown
        return result;

/*        StringBuffer message = new StringBuffer();
        for (Object o : alerts) {
            Map alert = (Map) o;
            message.append("\n");
            message.append(JsonHelper.getExpressionValue(alert, template, "json"));
        }
        return message.toString(); */

/*        String status = (String) webhook.get("status");
        List list = (List) webhook.get("alerts");
        int count = list.size();
        String message = JsonHelper.getExpressionValue(webhook, template, "json");
        return String.format("[%s:%d]\n%s", status, count, message); */
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
