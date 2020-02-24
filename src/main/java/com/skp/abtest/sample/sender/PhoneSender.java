package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.config.ConfigApplication;
import com.skp.abtest.sample.config.ConfigWebhook;
import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.SenderResponse;
import com.skp.abtest.sample.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class PhoneSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired HttpTransport httpTransport;
    @Value("${application.phone.url}") String url;
    @Value("${application.phone.method}") String method;
    @Value("${application.phone.body}") String body;
    @Value("${application.phone.format}") String format;

    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();

        SenderResponse response = notifyInternal(request);
        responseList.add(response);
        return responseList;
    }

    private SenderResponse notifyInternal(NotifyRequest request) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setName("phone");
        response.setResult(true);

        try {
            ResponseEntity<String> httpResponse;
            if ("GET".equals(method))
                httpResponse = httpTransport.sendGetRequest(buildUrl(request));
            else
                httpResponse = httpTransport.sendPostRequest(buildUrl(request), buildBody(request));
            response.setResult(httpResponse.getStatusCode().is2xxSuccessful());
            if (!response.isResult())
                response.setError(httpResponse.getBody());
        } catch (RuntimeException e) {
            logger.error(e.toString(), e);
            response.setResult(false);
        }
        return response;
    }

    private String buildUrl(NotifyRequest request) {
        return JsonHelper.getExpressionValue(request, url, format);
/*        String value = url;
        value = value.replace("{{ .phone }}", buildValueList(request.getPhone(), format));
        value = value.replace("{{ .title }}", request.getTitle());
        value = value.replace("{{ .message }}", request.getMessage());
        return value; */
    }

    private String buildBody(NotifyRequest request) {
        return JsonHelper.getExpressionValue(request, body, format);
/*        String value = body;
        value = value.replace("{{ .phone }}", buildValueList(request.getPhone(), format));
        value = value.replace("{{ .title }}", request.getTitle());
        value = value.replace("{{ .message }}", request.getMessage());
        return value; */
    }

    private String buildValueList(List<String> phoneList, String format) {
        if ("json".equals(format))
            return JsonHelper.writeValue(phoneList);
        return String.join(",", phoneList);
    }

}
