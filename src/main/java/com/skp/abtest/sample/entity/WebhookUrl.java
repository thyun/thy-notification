package com.skp.abtest.sample.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class WebhookUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_id")
    private String targetId;

//    @NotBlank(message = "Name is mandatory")
    private String name;
    private String method;
    private String url;
    private String body;

    @Override
    public String toString() {
        String result = String.format("[WebhookUrl] name=%s method=%s url=%s body=%s", name, method, url, body);
        return result;
    }
}
