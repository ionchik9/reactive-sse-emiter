package com.sse.emiter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GptChoice(GptMessage delta,
                        @JsonProperty("finish_reason")
                        String finishReason,
                        int index) {
}
