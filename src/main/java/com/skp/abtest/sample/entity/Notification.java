package com.skp.abtest.sample.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Notification {
    @Id
    private String id;

    @Column(length = 1024)
    private String email;

    @Column(length = 1024)
    private String slack;

    @Column(length = 1024)
    private String sms;

    public List<String> getEmailList() {
        if (email == null || email.length() == 0)
            return new ArrayList<>();
        return Arrays.asList(email.split(" "));
    }

    public List<SlackChannel> getSlackList() {
        if (slack == null || slack.length() == 0)
            return new ArrayList<>();

        return Arrays.asList(slack.split(" "))
                .stream().map((s) -> {
                    SlackChannel slackChannel = new SlackChannel();
                    slackChannel.setChannelKey(s);
                    return slackChannel;
                })
                .collect(Collectors.toList());
    }

    public List<String> getSmsList() {
        if (sms == null || sms.length() == 0)
            return new ArrayList<>();

        return Arrays.asList(sms.split(" "));
    }

}
