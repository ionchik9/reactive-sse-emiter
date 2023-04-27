package com.sse.emiter.controller;

import com.sse.emiter.model.MessageRequest;
import com.sse.emiter.service.OpenAICompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse-server")
@RequiredArgsConstructor
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

    @PostMapping("/stream-gpt")
    public SseEmitter streamSse(MessageRequest request) {
        SseEmitter emitter = new SseEmitter();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            try {
                while (true) {
                    String response = openAICompletionService.getCompletionResult(request.content()).get();
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(response)
                            .name("sse event");
                    emitter.send(event);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            } finally {
                emitter.complete();
            }
        });

        return emitter;
    }
}
