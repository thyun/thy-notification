package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.SenderResponse;
import com.skp.abtest.sample.entity.Slack;
import com.skp.abtest.sample.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class SlackSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${application.slack.body}") String body;

    HttpTransport httpTransport;
    public SlackSender(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
        for (Slack slack : request.getSlack()) {
            String url = buildUrl(request, slack);
            String body = buildBody(request, slack);
            logger.debug("<notify> slack: url={} body={}", url, body);
            SenderResponse response = notifyInternal(request, url, body);
            responseList.add(response);
        }
        return responseList;
    }

    private String buildUrl(NotifyRequest request, Slack slack) {
        return String.format("%s", slack.getUrl()).toString();
    }
    
    private String buildBody(NotifyRequest request, Slack channel) {
        return JsonHelper.getExpressionValue(request, body, "json");
//        return String.format("{ \"text\": \"%s\" }", buildTitleAndMessage(request.getTitle(), request.getMessage()));
    }

//    private Object buildTitleAndMessage(String title, String message) {
//        return String.format("[%s]\n%s", title, message);
//    }

    private SenderResponse notifyInternal(NotifyRequest request, String url, String body) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setName("slack");
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
