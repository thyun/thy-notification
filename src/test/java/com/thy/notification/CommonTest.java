package com.thy.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thy.notification.entity.Target;
import com.thy.notification.sender.MsteamsBody;
import com.thy.notification.util.FileHelper;
import com.thy.notification.util.JsonHelper;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class CommonTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testNotification() {
        Target n = new Target();
        n.setPhone("01010001000 01010001001");
        n.setEmail("aaaa@aaaa.com bbbb@bbbb.com");
        n.setSlack("T0NCP1206/aaaa T0NCP1206/bbbb");
        logger.debug("phoneList={}", n.getPhoneList());
        logger.debug("emailList={}", n.getEmailList());
        logger.debug("slackList={}", n.getSlackList());

        ArrayList<String> mergedList = new ArrayList<>();
        n.setEmail("");
        logger.debug("emailList size={}", n.getEmailList().size());
        mergedList.addAll(n.getEmailList());
        logger.debug("mergedList size={}", mergedList.size());
    }

    @Test
    public void testGetPathValue1() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request02.json"), Map.class);
        String value = JsonHelper.getPathValue(request, "{{ .title }}", "json");
        assertEquals("제목", value);

        value = JsonHelper.getPathValue(request, "{{ .phone }}", "json");
        assertEquals("[\"01010001001\",\"01010001002\"]", value);

        value = JsonHelper.getPathValue(request, "{{ .phone }}", "delimiter");
        assertEquals("01010001001,01010001002", value);
    }


    @Test
    public void testGetPathValue2() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("alertmanager-request01.json"), Map.class);
        String value = JsonHelper.getPathValue(request, "{{ .commonLabels.alertname }}", "json");
        assertEquals("High Memory Usage of Container", value);

//        value = JsonHelper.getPathValue(request, "default");
//        assertEquals("default", value);
    }

    @Test
    public void testGetExpressionValue() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("alertmanager-request01.json"), Map.class);
        String exp = "Alert: {{ .commonLabels.alertname }}. Summary: {{ .commonAnnotations.summary }}";
        String value = JsonHelper.getExpressionValue(request, exp, "json");
        assertEquals("Alert: High Memory Usage of Container. Summary: Container named  in  in default is using more than 75% of Memory Limit", value);
    }

    @Test
    public void testJsonValue() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("alertmanager-request01.json"), Map.class);
        List<Map> alerts = (ArrayList<Map>) request.get("alerts");
        Map alert = alerts.get(0);
        logger.debug("labels=" + JsonHelper.getPathValue(alert, "{{ .labels }}", "json"));
    }

    @Test
    public void testMsteamsBody() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("alertmanager-request01.json"), Map.class);
        List<Map> alerts = (ArrayList<Map>) request.get("alerts");
        Map alert = alerts.get(0);

        MsteamsBody body = new MsteamsBody();
        body.setContext("https://schema.org/extensions");
        body.setType("MessageCard");
        body.setTitle("");
        body.setText(JsonHelper.getPathValue(alert, "{{ .labels }}", "json"));
        logger.debug("body=" + JsonHelper.writeValue(body));
    }


}
