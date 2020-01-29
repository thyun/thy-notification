package com.skp.abtest.sample.entity;

import lombok.Data;

import java.util.List;

@Data
public class NotifyResponse {
    String id;
    boolean result;
    int status = 200;
    String error;

    List<SenderResponse> email;
    List<SenderResponse> slack;
    List<SenderResponse> sms;
}
