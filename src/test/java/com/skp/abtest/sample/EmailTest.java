package com.skp.abtest.sample;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailTest {
    @Ignore
    @Test
    public void testEmail() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("xxxx@gmail.com");
        mailSender.setPassword("zgtgfteyivkzrsdf");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        mailSender.send(buildMailMessage("xxxx@gmail.com", "th.yun@sk.com", "[제목]", "내용"));
    }

    private SimpleMailMessage buildMailMessage(String senderEmail, String receiverEmail, String subject, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(senderEmail);
        msg.setTo(receiverEmail);
        msg.setSubject(subject);
        msg.setText(message);
        return msg;
    }

}
