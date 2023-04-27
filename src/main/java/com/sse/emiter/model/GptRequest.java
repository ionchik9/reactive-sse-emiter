package com.sse.emiter.model;

import java.util.List;

public record GptRequest(String model, List<GptMessage> messages) {
}
