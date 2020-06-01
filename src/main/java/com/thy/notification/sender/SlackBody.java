package com.thy.notification.sender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SlackBody {
    String text;
}
