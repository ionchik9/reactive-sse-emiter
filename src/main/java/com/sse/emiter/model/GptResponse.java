package com.sse.emiter.model;

import java.util.List;

public record GptResponse(List<GptChoice> choices) {
}
