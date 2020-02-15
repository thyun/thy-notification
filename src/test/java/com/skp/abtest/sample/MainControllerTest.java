package com.skp.abtest.sample;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import com.skp.abtest.sample.entity.Notification;
import com.skp.abtest.sample.entity.NotificationRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/*
 * 참조: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(MainControllerTest.class);

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void testNotificationRepository() throws Exception {
        Notification n = new Notification();
        n.setId("default");
        n.setPhone("01010001000 01010001001 01010001002");
        notificationRepository.save(n);
    }

}
