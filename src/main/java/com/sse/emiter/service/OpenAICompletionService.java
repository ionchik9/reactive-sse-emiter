package com.sse.emiter.service;

import com.sse.emiter.client.OpenAiClient;
import com.sse.emiter.model.GptMessage;
import com.sse.emiter.model.GptRequest;
import com.sse.emiter.model.GptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAICompletionService {
    private final OpenAiClient openai;

    public Future<String> getCompletionResult(String req) {
        try {
            GptRequest completionRequest = new GptRequest("gpt-3.5-turbo", List.of(new GptMessage("user", req)), true);

//            GptResponse completionResponse = openai.submitMessage(completionRequest);

            log.info("will submit");
            GptResponse completionResponse = openai.submitMessage(new GptMessage("user", req));

            log.info("submitted, res {}", completionResponse);

            String response = completionResponse.choices().get(0).delta().content();
            return new CompletableFuture<String>().completedFuture(response);
        } catch (Exception ex) {
            return new CompletableFuture<String>().failedFuture(ex);
        }
    }
}
