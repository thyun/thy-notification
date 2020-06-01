package com.thy.notification.entity;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TargetRepositoryTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TargetRepository targetRepository;

    @Ignore
    @Test
    public void testSave() throws Exception {
        Target target = new Target();
        target.setKey("test");
        target.setPhone("01010001000 01010001001 01010001002");

        Webhook webhook = new Webhook();
        webhook.setName("webhook01");
        target.addWebhook(webhook);
        targetRepository.save(target);

        Iterable<Target> targetList = targetRepository.findAll();
        for (Target it : targetList) {
            logger.debug(it.toString());
        }

    }

}