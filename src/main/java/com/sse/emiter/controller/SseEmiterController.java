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

//		WebClient client = WebClient.create("http://localhost:8080/sse-server");
//
//        ParameterizedTypeReference<GptResponse> type
//                = new ParameterizedTypeReference<>() {
//        };
//
//         var eventStream = client.post()
//                .uri("/stream-gpt")
//                .body(BodyInserters.fromValue(Map.of("content", request.content())))
//                .retrieve()
//                .bodyToFlux(type)
//                 .doOnNext(content -> log.info("Received SSE event: name[{}], id [{}], content[{}]",
//                         content.choices().get(0).finishReason(),
//                         content.choices().get(0).index(),
//                         content.choices().get(0).delta().content()));
//
//        return eventStream;

        return openAICompletionService.requestCompletion(request);
    }
}
