package com.skp.abtest.sample;

import com.skp.abtest.sample.sender.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainBean {
    @Bean
    SenderManager senderManager() {
        return new SenderManager();
    }

    @Bean
    PhoneSender phoneSender() { return new PhoneSender(); }

    @Bean
    EmailSender emailSender(
            @Value("${application.email.host}") String host,
            @Value("${application.email.port}") int port,
            @Value("${application.email.auth}") boolean auth,
            @Value("${application.email.username}") String username,
            @Value("${application.email.password}") String password) {
        EmailSender emailSender = new EmailSender();
        emailSender.init(host, port, auth, username, password);
        return emailSender;
    }

    @Bean
    SlackSender slackSender(HttpTransport httpTransport) {
        return new SlackSender(httpTransport);
    }

    @Bean
    MsteamsSender msteamsSender(HttpTransport httpTransport) {
        return new MsteamsSender(httpTransport);
    }

    @Bean
    WebhookSender webhookSender() { return new WebhookSender(); }

    @Bean
    HttpTransport httpTransport(
            @Value("${application.proxy.use}") boolean proxyUse,
            @Value("${application.proxy.url}") String proxyUrl) {
        HttpTransport httpTransport = new HttpTransport();
        httpTransport.setProxy(proxyUse, proxyUrl);
        return httpTransport;
    }
}
