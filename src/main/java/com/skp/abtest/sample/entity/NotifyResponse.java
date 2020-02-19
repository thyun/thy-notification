package com.skp.abtest.sample.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class NotifyResponse {
    String id;
    boolean result;
    String error;
    int status = 200;

    List<SenderResponse> email;
    List<SenderResponse> slack;
    List<SenderResponse> webhook;
}
