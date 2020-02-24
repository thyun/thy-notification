package com.skp.abtest.sample.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.skp.abtest.sample.entity.Target;
import com.skp.abtest.sample.entity.TargetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.util.FileHelper;

import java.util.Optional;

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
	@MockBean(name="targetRepository")
    private TargetRepository targetRepository;

    // Test POST /v1/notify
    @Test
    public void testPostNotify() throws Exception {
        // Prepare
        String targetId = "default";
        given(targetRepository.findById(targetId)).willReturn(makeNotification(targetId));

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
        // Prepare
        String targetId = "team:devops";
        given(targetRepository.findById(targetId)).willReturn(makeNotification(targetId));

        String request =  FileHelper.getFileFromResource("webhook-request01.json");
        logger.debug("request=" + request);
        this.mockMvc.perform(post("/v1/webhook").contentType(MediaType.APPLICATION_JSON).content(request))
                .andDo(print())
                .andExpect(status().isOk());
//            .andExpect(jsonPath("$", hasSize(1)));
    }

    private Optional<Target> makeNotification(String targetId) {
        Target target = new Target();
        target.setId(targetId);
        target.setPhone("01010001001 01010001002");
//        target.setSlack("http://localhost:9000");
        return Optional.ofNullable(target);
    }
}
