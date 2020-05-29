package com.thy.notification.entity;

import lombok.Data;

import java.util.List;

@Data
public class NotifyResponse {
    String id;
    boolean result;
    String error = "";
    int status = 200;

    List<SenderResponse> phone;
    List<SenderResponse> email;
    List<SenderResponse> slack;
    List<SenderResponse> msteams;
    List<SenderResponse> webhook;
}
