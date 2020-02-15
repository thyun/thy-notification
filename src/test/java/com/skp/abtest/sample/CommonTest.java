package com.skp.abtest.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skp.abtest.sample.entity.Notification;
import com.skp.abtest.sample.util.FileHelper;
import com.skp.abtest.sample.util.JsonHelper;
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
        Notification n = new Notification();
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
    public void testGetExpValue() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("webhook-request01.json"), Map.class);
        String exp = "Alert: {{ .commonLabels.alertname }}. Summary: {{ .commonAnnotations.summary }}";
        String value = JsonHelper.getExpValue(request, exp);
        assertEquals("Alert: High Memory Usage of Container. Summary: Container named  in  in default is using more than 75% of Memory Limit", value);
    }

    @Test
    public void testGetPathValue() throws IOException {
        Map request = objectMapper.readValue(FileHelper.getFileFromResource("webhook-request01.json"), Map.class);
        String value = JsonHelper.getPathValue(request, "{{ .commonLabels.alertname }}");
        assertEquals("High Memory Usage of Container", value);

        value = JsonHelper.getPathValue(request, "default");
        assertEquals("default", value);
    }

}
