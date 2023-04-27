package com.sse.emiter.controller;

import com.sse.emiter.client.OpenAiReactiveClient;
import com.sse.emiter.model.GptResponse;
import com.sse.emiter.model.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/sse-server")
@RequiredArgsConstructor
@Slf4j
public class SseEmiterController {
    private final OpenAiReactiveClient openAICompletionService;


    @PostMapping(value = "/stream-gpt-react", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GptResponse> streamSse(@RequestBody MessageRequest request) {

        return openAICompletionService.requestCompletion(request);
    }
}
