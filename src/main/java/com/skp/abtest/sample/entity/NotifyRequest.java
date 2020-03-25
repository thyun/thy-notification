package com.skp.abtest.sample.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class NotifyRequest {
    String id;
    String targetId;
    String title;
    String message;
    List<String> phone = new ArrayList<>();
    List<String> email = new ArrayList<>();
    List<Slack> slack = new ArrayList<>();
    List<Msteams> msteams = new ArrayList<>();
    List<Webhook> webhook = new ArrayList<>();
//    List<Object> telegram;
//    List<Object> kakaotalk;
}
