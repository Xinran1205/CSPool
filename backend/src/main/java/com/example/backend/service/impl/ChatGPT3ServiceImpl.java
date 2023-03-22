package com.example.backend.service.impl;

import com.example.backend.service.ChatGPT3Service;
import io.lettuce.core.dynamic.annotation.Value;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/22 22:14
 */

@Service
public class ChatGPT3ServiceImpl implements ChatGPT3Service {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient httpClient = new OkHttpClient();

    //这个key我们写在配置文件中，从配置文件中注入，如果是微服务我们放在nacos
//    @Value("${chatgpt3.api.key}")
    private String apiKey;

    public String getResponse(String prompt) throws IOException {
        String url = "https://api.openai.com/v1/engines/davinci-codex/completions";
        String model = "davinci-codex";
        int maxTokens = 100;
        boolean stop = true;
        String requestBody = "{\"model\": \"" + model + "\", \"prompt\": \"" + prompt + "\", \"max_tokens\": " + maxTokens + ", \"stop\": " + stop + "}";
        RequestBody body = RequestBody.create(requestBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }
}

