package com.skp.abtest.sample.entity;

import lombok.Data;

@Data
public class SenderResponse {
    String id;
    String name;
    boolean result;
    String error = "";
}
