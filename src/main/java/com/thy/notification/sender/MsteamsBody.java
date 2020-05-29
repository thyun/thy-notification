package com.thy.notification.sender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MsteamsBody {
    @JsonProperty("@context")
    String context;
    @JsonProperty("@type")
    String type;
    String title;
    String text;
}
