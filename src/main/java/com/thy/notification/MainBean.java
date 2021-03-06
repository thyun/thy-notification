package com.thy.notification;

import com.thy.notification.sender.*;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

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

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }

/*    @Bean
    public Validator validator () {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    } */

}
