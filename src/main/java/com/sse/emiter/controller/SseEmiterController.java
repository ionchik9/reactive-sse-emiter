package com.sse.emiter.controller;

import com.sse.emiter.model.GptResponse;
import com.sse.emiter.model.MessageRequest;
import com.sse.emiter.service.OpenAICompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse-server")
@RequiredArgsConstructor
@Slf4j
public class SseEmiterController {
    private final OpenAICompletionService openAICompletionService;



    @GetMapping("/stream-sse")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newFixedThreadPool(6);
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now())
                            .id(String.valueOf(i))
                            .name("sse event - mvc");
                    emitter.send(event);
                    Thread.sleep(2000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @PostMapping(value = "/stream-gpt-react", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GptResponse> streamSse(MessageRequest request) {

		WebClient client = WebClient.create("http://localhost:8080/sse-server");

        ParameterizedTypeReference<GptResponse> type
                = new ParameterizedTypeReference<>() {
        };

         var eventStream = client.post()
                .uri("/stream-gpt")
                .body(BodyInserters.fromValue(Map.of("content", "dough")))
                .retrieve()
                .bodyToFlux(type)
                 .doOnNext(content -> log.info("Received SSE event: name[{}], id [{}], content[{}]",
                         content.choices().get(0).finishReason(),
                         content.choices().get(0).index(),
                         content.choices().get(0).delta().content()));

        return eventStream;
    }
}
