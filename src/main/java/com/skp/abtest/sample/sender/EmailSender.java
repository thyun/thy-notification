package com.skp.abtest.sample.sender;

import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.SenderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    JavaMailSenderImpl mailSender;

    public EmailSender() {
        mailSender = new JavaMailSenderImpl();
    }

    // TODO Set from
    public List<SenderResponse> notify(NotifyRequest request) {
        ArrayList<SenderResponse> responseList = new ArrayList<>();
        if (request.getEmail() == null || request.getEmail().size() == 0)
            return responseList;

        String from = "no-reply@com";
        String[] to = buildTo(request.getEmail());

        SenderResponse response = notifyInternal(request, from, to);
        responseList.add(response);
        return responseList;
    }

    private String[] buildTo(List<String> emailList) {
        return emailList.toArray(new String[0]);
        //return String.join(", ", emailList);
    }

    private SenderResponse notifyInternal(NotifyRequest request, String from, String[] to) {
        SenderResponse response = new SenderResponse();
        response.setId(request.getId());
        response.setName("email");
        response.setResult(true);
        try {
            mailSender.send(buildMailMessage(from, to, request.getTitle(), request.getMessage()));
        } catch (RuntimeException e) {
            logger.error(e.toString());
            response.setResult(false);
        }
        return response;
    }

    private SimpleMailMessage buildMailMessage(String from, String[] to, String title, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(title);
        msg.setText(message);
        return msg;
    }

    public void init(String host, int port, boolean auth, String username, String password) {
        Properties props = mailSender.getJavaMailProperties();

        mailSender.setHost(host);
        mailSender.setPort(port);

        props.put("mail.transport.protocol", "smtp");
        if (auth) {
            props.put("mail.smtp.auth", "true");
            mailSender.setUsername(username);
            mailSender.setPassword(password);
        } else
            props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
    }
}
