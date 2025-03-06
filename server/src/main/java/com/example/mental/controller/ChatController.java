package com.example.mental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private static final String AI_SERVICE_URL = "http://127.0.0.1:8081/completions";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private static final Gson gson = new Gson();

    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getCompletion(@org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
        SseEmitter emitter = new SseEmitter();
        
        try {
            String userMessage = request.get("message");
            log.info("Received message: {}", userMessage);
            
            String formattedPrompt = String.format("<用户>%s<AI>", userMessage);
            Map<String, Object> aiRequest = new HashMap<>();
            aiRequest.put("prompt", formattedPrompt);
            aiRequest.put("n_predict", 256);
            aiRequest.put("stream", true);

            String requestBody = gson.toJson(aiRequest);
            log.info("Sending request to AI service: {}", requestBody);

            okhttp3.Request aiServiceRequest = new okhttp3.Request.Builder()
                    .url(AI_SERVICE_URL)
                    .post(okhttp3.RequestBody.create(
                            requestBody,
                            okhttp3.MediaType.parse("application/json; charset=utf-8")
                    ))
                    .build();

            client.newCall(aiServiceRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("Request failed", e);
                    emitter.completeWithError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    log.info("Received response from AI service: {}", response.code());
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful() || responseBody == null) {
                            String error = "Unexpected response: " + response;
                            log.error(error);
                            emitter.completeWithError(new IOException(error));
                            return;
                        }

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(responseBody.byteStream())
                        );

                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                log.debug("Sending line to client: {}", line);
                                emitter.send(line);
                            }
                        }
                        log.info("Completed streaming response");
                        emitter.complete();
                    } catch (Exception e) {
                        log.error("Error while streaming response", e);
                        emitter.completeWithError(e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Error in controller", e);
            emitter.completeWithError(e);
        }

        return emitter;
    }
} 