package com.thy.notification.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_id")
    private String targetId;

//    @NotBlank(message = "Name is mandatory")
    private String name;
    private String url;
    private String method;
    private String body;
    private String format = "json";

    public boolean isValid() {
        return (name != null && name.length() > 0 &&
                url != null && url.length() > 0);
    }

    @Override
    public String toString() {
        String result = String.format("[Webhook] name=%s method=%s url=%s body=%s", name, method, url, body);
        return result;
    }
}
