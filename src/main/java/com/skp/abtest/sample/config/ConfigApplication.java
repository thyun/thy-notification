package com.skp.abtest.sample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix="application")
public class ConfigApplication {

    private List<ConfigWebhook> webhook = new ArrayList<ConfigWebhook>();

    public List<ConfigWebhook> getWebhook() {
        return this.webhook;
    }
}
