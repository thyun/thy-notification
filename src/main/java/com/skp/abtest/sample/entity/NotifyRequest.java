package com.skp.abtest.sample.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class NotifyRequest {
    String id;
    String notificationId;
    String title;
    String message;
    List<String> email = new ArrayList<>();
    List<SlackChannel> slack = new ArrayList<>();
    List<Object> telegram;
    List<Object> kakaotalk;
    List<String> sms = new ArrayList<>();
}