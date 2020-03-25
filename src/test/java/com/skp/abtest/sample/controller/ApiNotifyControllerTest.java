package com.skp.abtest.sample.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.skp.abtest.sample.entity.*;
import com.skp.abtest.sample.sender.*;
import org.junit.Before;
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
import com.skp.abtest.sample.util.FileHelper;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
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
    @MockBean(name="phoneSender")
    private PhoneSender phoneSender;
    @MockBean(name="emailSender")
    private EmailSender emailSender;
    @MockBean(name="slackSender")
    private SlackSender slackSender;
    @MockBean(name="msteamsSender")
    private MsteamsSender msteamsSender;
    @MockBean(name="webhookSender")
    private WebhookSender webhookSender;

    String targetId = "default";
    Target target;

    @Before
    public void init() {
        target = makeTarget("default");
    }

    //////////////////////////////////////////////////////////////////////
    // Test POST /v1/notify
    //////////////////////////////////////////////////////////////////////
    @Test
    public void testNotifyByPhone() throws Exception {
        NotifyRequest request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request01.json"), NotifyRequest.class);
        logger.debug("request=" + request);

        // Prepare
        target.setPhone("01010001001 01010001002");
        given(targetRepository.findById(targetId)).willReturn(Optional.ofNullable(target));
        given(phoneSender.notify(any(NotifyRequest.class))).willReturn(makeSenderResponseList(request));

        // Perform
        this.mockMvc.perform(post("/v1/notify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone[0].id").value("send-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone[0].result").value(true));
    }

    @Test
    public void testNotifyByEmail() throws Exception {
        NotifyRequest request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request01.json"), NotifyRequest.class);
        logger.debug("request=" + request);

        // Prepare
        target.setEmail("aaaa@example.com bbbb@example.com");
        given(targetRepository.findById(targetId)).willReturn(Optional.ofNullable(target));
        given(emailSender.notify(any(NotifyRequest.class))).willReturn(makeSenderResponseList(request));

        // Perform
        this.mockMvc.perform(post("/v1/notify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email[0].id").value("send-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email[0].result").value(true));
    }

    @Test
    public void testNotifyBySlack() throws Exception {
        NotifyRequest request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request01.json"), NotifyRequest.class);
        logger.debug("request=" + request);

        // Prepare
        target.setSlack("https://hooks.slack.com/services/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");
        given(targetRepository.findById(targetId)).willReturn(Optional.ofNullable(target));
        given(slackSender.notify(any(NotifyRequest.class))).willReturn(makeSenderResponseList(request));

        // Perform
        this.mockMvc.perform(post("/v1/notify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slack", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slack[0].id").value("send-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slack[0].result").value(true));
    }

    @Test
    public void testNotifyByMsteams() throws Exception {
        NotifyRequest request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request01.json"), NotifyRequest.class);
        logger.debug("request=" + request);

        // Prepare
        target.setMsteams("https://outlook.office.com/webhook/1f10ad42-952a-4f67-82b5-0dd15135fd89@c0e04683-773c-43be-a906-36be87292cc2/IncomingWebhook/21f9bc48490747a8aa05b350296f853a/27206cbd-4df7-424c-b060-57b7eb770d83");
        given(targetRepository.findById(targetId)).willReturn(Optional.ofNullable(target));
        given(msteamsSender.notify(any(NotifyRequest.class))).willReturn(makeSenderResponseList(request));

        // Perform
        this.mockMvc.perform(post("/v1/notify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msteams", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msteams[0].id").value("send-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msteams[0].result").value(true));
    }

    @Test
    public void testNotifyByWebhook() throws Exception {
        NotifyRequest request = objectMapper.readValue(FileHelper.getFileFromResource("notify-request01.json"), NotifyRequest.class);
        logger.debug("request=" + request);

        // Prepare
        List<Webhook> webhookList = new ArrayList<>();
        Webhook webhook = makeWebhook();
        webhookList.add(webhook);
        target.setWebhookList(webhookList);
        given(targetRepository.findById(targetId)).willReturn(Optional.ofNullable(target));
        given(webhookSender.notify(any(NotifyRequest.class))).willReturn(makeSenderResponseList(request));

        // Perform
        this.mockMvc.perform(post("/v1/notify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.webhook", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.webhook[0].id").value("send-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.webhook[0].result").value(true));
    }


    //////////////////////////////////////////////////////////////////////
    // Test POST /v1/webhook
    //////////////////////////////////////////////////////////////////////
    @Test
    public void testWebhook() throws Exception {
        // Prepare
        String targetId = "team:devops";
        Target target = makeTarget(targetId);
        given(targetRepository.findById(targetId)).willReturn(Optional.ofNullable(target));

        String request =  FileHelper.getFileFromResource("webhook-request01.json");
        logger.debug("request=" + request);
        this.mockMvc.perform(post("/v1/webhook").contentType(MediaType.APPLICATION_JSON).content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Target makeTarget(String targetId) {
        Target target = new Target();
        target.setId(targetId);
        return target;
    }

    private List<SenderResponse> makeSenderResponseList(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setResult(true);
        responseList.add(response);
        return responseList;
    }

    private Webhook makeWebhook() {
        Webhook webhook = new Webhook();
        webhook.setUrl("http://localhost");
        return webhook;
    }
}
