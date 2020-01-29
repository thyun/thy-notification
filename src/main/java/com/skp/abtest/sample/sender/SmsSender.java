package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.SenderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SmsSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    HttpTransport httpTransport;
    String url;
    String method;
    String bodyPath;

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
//        String phoneNumbers = buildPhoneNumbers(request.getSms());

        SenderResponse response = notifyInternal(request);
        responseList.add(response);
        return responseList;
    }

    private String buildPhoneNumbers(List<String> smsList) {
        return String.join(",", smsList);
    }

    private SenderResponse notifyInternal(NotifyRequest request) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setResult(true);

        try {
            httpTransport.sendGet(buildUrl(request));
        } catch (RuntimeException e) {
            logger.error(e.toString());
            response.setResult(false);
        }
        return response;
    }

    // TODO Use JsonHelper.getExpValue() - Change NotifyRequest -> Map?
    private String buildUrl(NotifyRequest request) {
        String value = url;
        value = value.replace("{{ .sms }}", buildPhoneNumbers(request.getSms()));
        value = value.replace("{{ .title }}", request.getTitle());
        return value.replace("{{ .message }}", request.getMessage());
    }

    public void init(String url, String method, String bodyPath) {
        logger.debug("SmsSender.init(): url={}", url);
        this.url = url;
        this.method = method;
        this.bodyPath = bodyPath;
    }
}
