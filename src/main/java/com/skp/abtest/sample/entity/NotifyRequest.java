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
    List<String> phones = new ArrayList<>();
    List<String> email = new ArrayList<>();
    List<SlackChannel> slack = new ArrayList<>();
    List<Object> telegram;
    List<Object> kakaotalk;
}
