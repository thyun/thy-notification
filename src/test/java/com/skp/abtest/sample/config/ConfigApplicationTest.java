package com.skp.abtest.sample.config;

import com.skp.abtest.sample.entity.Target;
import com.skp.abtest.sample.entity.TargetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConfigApplicationTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigApplication configApplication;

    @Test
    public void testConfig() throws Exception {
        logger.info("configWebhook=" + configApplication.getWebhook());
    }

}
