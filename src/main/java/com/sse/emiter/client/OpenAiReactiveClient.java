package com.sse.emiter.client;

import com.sse.emiter.model.GptResponse;
import com.sse.emiter.model.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Component
@Slf4j
public class OpenAiReactiveClient {

    public Flux<GptResponse> requestCompletion(MessageRequest request) {

        WebClient client = WebClient.create("http://localhost:8080/sse-server");

        ParameterizedTypeReference<GptResponse> type
                = new ParameterizedTypeReference<>() {
        };

        var eventStream = client.post()
                .uri("/stream-gpt")
                .body(BodyInserters.fromValue(Map.of("content", request.content())))
                .retrieve()
                .bodyToFlux(type)
                .doOnNext(content -> log.info("Received SSE event: name[{}], id [{}], content[{}]",
                        content.choices().get(0).finishReason(),
                        content.choices().get(0).index(),
                        content.choices().get(0).delta().content()));

        return eventStream;
    }
}
