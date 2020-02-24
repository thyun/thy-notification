package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.SenderResponse;
import com.skp.abtest.sample.entity.SlackRequest;
import com.skp.abtest.sample.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class MsteamsSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${application.msteams.body}") String body;

    HttpTransport httpTransport;
    public MsteamsSender(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
        for (SlackRequest slackRequest: request.getSlack()) {
            String url = buildUrl(request, slackRequest);
            String body = buildBody(request, slackRequest);
            logger.debug("<notify> msteams: url={} body={}", url, body);
            SenderResponse response = notifyInternal(request, url, body);
            responseList.add(response);
        }
        return responseList;
    }

    private String buildUrl(NotifyRequest request, SlackRequest slackRequest) {
        return String.format("%s", slackRequest.getUrl()).toString();
    }
    
    private String buildBody(NotifyRequest request, SlackRequest channel) {
        return JsonHelper.getExpressionValue(request, body, "json");
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
