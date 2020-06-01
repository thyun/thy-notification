package com.thy.notification.sender;

import com.thy.notification.entity.Msteams;
import com.thy.notification.entity.NotifyRequest;
import com.thy.notification.entity.SenderResponse;
import com.thy.notification.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class MsteamsSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // body: '{ "@context":"https://schema.org/extensions", "@type":"MessageCard", "themeColor":"0072C6", "title":"{{ .title }}", "text":"{{ .message }}" }'
    // @Value("${application.msteams.body}") String body;

    HttpTransport httpTransport;
    public MsteamsSender(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
        for (Msteams channel : request.getMsteams()) {
            String url = buildUrl(request, channel);
            String body = buildBody(request, channel);
            logger.debug("<notify> msteams: url={} body={}", url, body);
            SenderResponse response = notifyInternal(request, url, body);
            responseList.add(response);
        }
        return responseList;
    }

    private String buildUrl(NotifyRequest request, Msteams channel) {
        return String.format("%s", channel.getUrl()).toString();
    }
    
    private String buildBody(NotifyRequest request, Msteams channel) {
        MsteamsBody body = new MsteamsBody();
        body.setContext("https://schema.org/extensions");
        body.setType("MessageCard");
        body.setTitle("");
        body.setText(JsonHelper.getExpressionValue(request, "{{ .message }}", "json"));
        return JsonHelper.writeValue(body);
//        return JsonHelper.getExpressionValue(request, body, "json");
    }

    private SenderResponse notifyInternal(NotifyRequest request, String url, String body) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setName("msteams");
        response.setResult(true);
        try {
            boolean result = httpTransport.sendPost(url, body);
        } catch (RuntimeException e) {
            logger.error(e.toString());
            response.setResult(false);
        }
        return response;
    }

}
