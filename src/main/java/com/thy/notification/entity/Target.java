package com.thy.notification.entity;

import com.thy.notification.validation.Unique;
import com.thy.notification.validation.UrlOrNull;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Key is mandatory")
    @Unique(entity = Target.class, fieldName = "key", message = "Key is duplicate")
    @Column(unique=true)    // Just add unique constraint to DB column
    private String key;

    @Column(length = 1024)
    private String phone;

    @Column(length = 1024)
    private String email;

    @UrlOrNull
    @Column(length = 1024)
    private String slack;

    @UrlOrNull
    @Column(length = 1024)
    private String msteams;

    @Valid
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "target_id")
    private Collection<Webhook> webhookList = new ArrayList<>();

    public List<String> getPhoneList() {
        if (phone == null || phone.length() == 0)
            return new ArrayList<>();

        return Arrays.asList(phone.split(" "));
    }

    public List<String> getEmailList() {
        if (email == null || email.length() == 0)
            return new ArrayList<>();
        return Arrays.asList(email.split(" "));
    }

    public List<Slack> getSlackList() {
        if (slack == null || slack.length() == 0)
            return new ArrayList<>();

        return Arrays.asList(slack.split(" "))
                .stream().map((s) -> {
                    Slack request = new Slack();
                    request.setUrl(s);
                    return request;
                })
                .collect(Collectors.toList());
    }

    public List<Msteams> getMsteamsList() {
        if (msteams == null || msteams.length() == 0)
            return new ArrayList<>();

        return Arrays.asList(msteams.split(" "))
                .stream().map((s) -> {
                    Msteams request = new Msteams();
                    request.setUrl(s);
                    return request;
                })
                .collect(Collectors.toList());
    }

    public void validate() {
        if (webhookList == null || webhookList.size() == 0)
            webhookList = new ArrayList<>();
        else
            webhookList = webhookList.stream()
                .filter(webhook -> webhook.isValid())
                .collect(Collectors.toList());
    }

    public void addWebhook(Webhook w) {
//        if (webhookUrls == null)
//            webhookUrls = new ArrayList<>();
        webhookList.add(w);
    }

    @Override
    public String toString() {
        String result = "[Target] id=" + id;
        for (Webhook webhook : webhookList) {
            result += "\n" + webhook.toString();
        }
        return result;
    }

}
