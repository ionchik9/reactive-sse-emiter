package com.sse.emiter.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;
import java.util.Map;

@Component
@Slf4j
public class OpenAiReactiveClient {


    public void consumeServerSentEvent() {
        WebClient client = WebClient.create("http://localhost:8085/sse-server");
//		WebClient client = WebClient.create("http://localhost:8080/sse-server");

        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        Flux<ServerSentEvent<String>> eventStream = client.post()
                .uri("/stream-gpt")
                .body(BodyInserters.fromValue(Map.of("content", "dough")))
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!"));
    }
}
