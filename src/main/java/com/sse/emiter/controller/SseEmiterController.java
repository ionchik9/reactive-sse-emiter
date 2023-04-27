package com.sse.emiter.controller;

import com.sse.emiter.model.GptChoice;
import com.sse.emiter.model.GptMessage;
import com.sse.emiter.model.GptResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/sse-server")
public class SseEmiterController {

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(sequence -> "Flux - " + LocalTime.now());
    }


    @PostMapping(path = "/stream-gpt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GptResponse> streamGpt(GptMessage message) {
        return Flux.interval(Duration.ofSeconds(2))
                .take(10) // limit the emissions to 10
                .map(sequence -> new GptResponse(List.of(new GptChoice(new GptMessage("assistant", message+ " now is " + LocalTime.now()), null, 0))));
    }


    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now())
                        .build());
    }
}
