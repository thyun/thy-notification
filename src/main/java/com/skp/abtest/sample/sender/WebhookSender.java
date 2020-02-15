package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.config.ConfigApplication;
import com.skp.abtest.sample.config.ConfigWebhook;
import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.SenderResponse;
import com.skp.abtest.sample.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class WebhookSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ConfigApplication configApplication;
    @Autowired
    HttpTransport httpTransport;

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();

        for (ConfigWebhook configWebhook: configApplication.getWebhook()) {
            SenderResponse response = notifyInternal(request, configWebhook);
            responseList.add(response);
        }
        return responseList;
    }

    private SenderResponse notifyInternal(NotifyRequest request, ConfigWebhook configWebhook) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setResult(true);

        try {
            if ("GET". equals(configWebhook.getMethod()))
                httpTransport.sendGet(buildUrl(request, configWebhook));
            else if ("POST".equals(configWebhook.getMethod()))
                httpTransport.sendPost(buildUrl(request, configWebhook), buildBody(request, configWebhook));

        } catch (RuntimeException e) {
            logger.error(e.toString());
            response.setResult(false);
        }
        return response;
    }

    // TODO Use JsonHelper.getExpValue() - Change NotifyRequest -> Map?
    private String buildUrl(NotifyRequest request, ConfigWebhook configWebhook) {
        String value = configWebhook.getUrl();
        value = value.replace("{{ .title }}", request.getTitle());
        value = value.replace("{{ .message }}", request.getMessage());
        value = value.replace("{{ .phone }}", buildValueList(request.getPhones(), configWebhook.getFormat()));
        return value;
    }

    private String buildBody(NotifyRequest request, ConfigWebhook configWebhook) {
        String value = configWebhook.getUrl();
        value = value.replace("{{ .title }}", request.getTitle());
        value = value.replace("{{ .message }}", request.getMessage());
        value = value.replace("{{ .phone }}", buildValueList(request.getPhones(), configWebhook.getFormat()));
        return value;
    }

    private String buildValueList(List<String> phoneList, String format) {
        if ("json".equals(format))
            return JsonHelper.writeValue(phoneList);
        return String.join(",", phoneList);
    }

}
