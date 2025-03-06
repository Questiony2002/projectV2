package com.example.projectv2.service;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.util.concurrent.TimeUnit;

public class AiService {
    private static final String TAG = "AiService";
    private static final String BASE_URL = "http://10.0.2.2:8081/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                Request request = chain.request();
                Log.d(TAG, "Sending request: " + request.url());
                Log.d(TAG, "Request body: " + request.body().toString());
                Response response = chain.proceed(request);
                Log.d(TAG, "Received response: " + response.code());
                return response;
            })
            .build();
    private static final Gson gson = new Gson();

    public interface AiResponseCallback {
        void onResponse(String response);
        void onComplete();
        void onError(String error);
    }

    public static void getAiResponse(String userMessage, AiResponseCallback callback) {
        try {
            String formattedPrompt = String.format("<|im_start|>user\\n%s<|im_end|>\\n<|im_start|>assistant\\n", userMessage);
            JsonObject jsonBody = new JsonObject();
            jsonBody.addProperty("prompt", formattedPrompt);
            jsonBody.addProperty("n_predict", 256);
            jsonBody.addProperty("stream", true);
            
            JsonArray stopArray = new JsonArray();
            stopArray.add("<|im_start|>");
            stopArray.add("<|im_end|>");
            jsonBody.add("stop", stopArray);
            
            jsonBody.addProperty("temperature", 0.6);
            jsonBody.addProperty("top_p", 0.7);
            
            String requestBodyStr = jsonBody.toString();
            Log.d(TAG, "Request body: " + requestBodyStr);

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(RequestBody.create(requestBodyStr, JSON))
                    .build();

            Log.d(TAG, "Executing request to: " + BASE_URL);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Request failed", e);
                    callback.onError("请求失败: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Response code: " + response.code());
                    if (!response.isSuccessful()) {
                        callback.onError("服务器错误: " + response.code());
                        return;
                    }

                    try {
                        ResponseBody responseBody = response.body();
                        if (responseBody == null) {
                            callback.onError("响应为空");
                            return;
                        }

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(responseBody.byteStream())
                        );

                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String jsonData = line.substring(6);
                                try {
                                    JsonObject jsonResponse = gson.fromJson(jsonData, JsonObject.class);
                                    String content = jsonResponse.get("content").getAsString();
                                    boolean stop = jsonResponse.get("stop").getAsBoolean();

                                    if (!content.isEmpty()) {
                                        callback.onResponse(content);
                                    }

                                    if (stop) {
                                        callback.onComplete();
                                        break;
                                    }
                                } catch (Exception e) {
                                    Log.e("AiService", "Error parsing JSON: " + e.getMessage());
                                }
                            }
                        }

                        reader.close();
                        responseBody.close();
                    } catch (Exception e) {
                        callback.onError("处理响应失败: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating request", e);
            callback.onError("创建请求失败: " + e.getMessage());
        }
    }
} 