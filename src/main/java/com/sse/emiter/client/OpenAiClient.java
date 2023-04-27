package com.sse.emiter.client;

import com.sse.emiter.model.GptMessage;
import com.sse.emiter.model.GptRequest;
import com.sse.emiter.model.GptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "openai", url = "${open-ai.url}")
public interface OpenAiClient {

    @PostMapping(value = "${open-ai.completion-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    GptResponse submitMessage(GptRequest request);
    @PostMapping(value = "${open-ai.completion-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    GptResponse submitMessage(GptMessage message);
//    GptResponse submitMessage(@RequestHeader("Authorization") String token, GptRequest request);
}
