package com.thy.notification.sender;

import com.thy.notification.config.ConfigApplication;
import com.thy.notification.config.ConfigWebhook;
import com.thy.notification.entity.NotifyRequest;
import com.thy.notification.entity.SenderResponse;
import com.thy.notification.entity.Webhook;
import com.thy.notification.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class WebhookSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ConfigApplication configApplication;
    @Autowired HttpTransport httpTransport;

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
        for (Webhook webhook : request.getWebhook()) {
            logger.debug("webhook={}" + webhook);
            SenderResponse response = notifyInternal(request, webhook);
            responseList.add(response);
 /*           String url = buildUrl(request, slack);
            String body = buildBody(request, slack);
            logger.debug("<notify> msteams: url={} body={}", url, body);
            SenderResponse response = notifyInternal(request, url, body);
            responseList.add(response); */
        }
        return responseList;
    }

    public List<SenderResponse> notifyConfig(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();

        for (ConfigWebhook configWebhook: configApplication.getWebhook()) {
            logger.debug("configWebhook={}", configWebhook);
            SenderResponse response = notifyConfigInternal(request, configWebhook);
            responseList.add(response);
        }
        return responseList;
    }

    private SenderResponse notifyInternal(NotifyRequest request, Webhook webhook) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setName(webhook.getName());
        response.setResult(true);

        try {
            ResponseEntity<String> httpResponse;
            if ("GET".equals(webhook.getMethod()))
                httpResponse = httpTransport.sendGetRequest(buildUrl(request, webhook));
            else
                httpResponse = httpTransport.sendPostRequest(buildUrl(request, webhook), buildBody(request, webhook));
            response.setResult(httpResponse.getStatusCode().is2xxSuccessful());
            if (!response.isResult())
                response.setError(httpResponse.getBody());
        } catch (RuntimeException e) {
            logger.error(e.toString(), e);
            response.setResult(false);
        }
        return response;
    }

    private SenderResponse notifyConfigInternal(NotifyRequest request, ConfigWebhook configWebhook) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setName(configWebhook.getName());
        response.setResult(true);

        try {
            ResponseEntity<String> httpResponse;
            if ("GET".equals(configWebhook.getMethod()))
                httpResponse = httpTransport.sendGetRequest(buildUrl(request, configWebhook));
            else
                httpResponse = httpTransport.sendPostRequest(buildUrl(request, configWebhook), buildBody(request, configWebhook));
            response.setResult(httpResponse.getStatusCode().is2xxSuccessful());
            if (!response.isResult())
                response.setError(httpResponse.getBody());
        } catch (RuntimeException e) {
            logger.error(e.toString(), e);
            response.setResult(false);
        }
        return response;
    }

    private String buildUrl(NotifyRequest request, Webhook webhook) {
        return JsonHelper.getExpressionValue(request, webhook.getUrl(), webhook.getFormat());
    }

    private String buildBody(NotifyRequest request, Webhook webhook) {
        return JsonHelper.getExpressionValue(request, webhook.getBody(), webhook.getFormat());
    }

    private String buildUrl(NotifyRequest request, ConfigWebhook configWebhook) {
        return JsonHelper.getExpressionValue(request, configWebhook.getUrl(), configWebhook.getFormat());
    }

    private String buildBody(NotifyRequest request, ConfigWebhook configWebhook) {
        return JsonHelper.getExpressionValue(request, configWebhook.getBody(), configWebhook.getFormat());
    }

}
