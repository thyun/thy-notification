package com.skp.abtest.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.util.FileHelper;

/*
 * 참조: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiNotifyControllerTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

    // Test POST /v1/notify
    @Test
    public void testPostNotify() throws Exception {
        NotifyRequest request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request01.json"), NotifyRequest.class);
        logger.debug("request=" + request);
        this.mockMvc.perform(post("/v1/notify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
//            .andExpect(jsonPath("$", hasSize(1)));
    }

    // Test POST /v1/webhook
    @Test
    public void testPostWebhook() throws Exception {
        String request =  FileHelper.getFileFromResource("webhook-request01.json");
        logger.debug("request=" + request);
        this.mockMvc.perform(post("/v1/webhook").contentType(MediaType.APPLICATION_JSON).content(request))
                .andDo(print())
                .andExpect(status().isOk());
//            .andExpect(jsonPath("$", hasSize(1)));
    }
}
