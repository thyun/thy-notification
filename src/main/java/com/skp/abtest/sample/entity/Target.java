package com.skp.abtest.sample.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
public class Target {
    @Id
    @NotBlank(message = "ID is mandatory")
    private String id;

    @Column(length = 1024)
    private String phone;

    @Column(length = 1024)
    private String email;

    @Column(length = 1024)
    private String slack;

    @Valid
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "target_id")
    private Collection<WebhookUrl> webhookUrls = new ArrayList<>();

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

    public List<String> getPhoneList() {
        if (phone == null || phone.length() == 0)
            return new ArrayList<>();

        return Arrays.asList(phone.split(" "));
    }

    public void addWebhookUrl(WebhookUrl w) {
//        if (webhookUrls == null)
//            webhookUrls = new ArrayList<>();
        webhookUrls.add(w);
    }

    @Override
    public String toString() {
        String result = "[Target] id=" + id;
        for (WebhookUrl webhookUrl: webhookUrls) {
            result += "\n" + webhookUrl.toString();
        }
        return result;
    }

}
