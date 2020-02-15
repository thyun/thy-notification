package com.skp.abtest.sample.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigWebhook {
    String name;
    String url;
    String method;
    String body;
    String format;
}
