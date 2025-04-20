package com.example.projectv2;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;
import java.io.File;

public class LLamaAPI {
    private static final String TAG = LLamaAPI.class.getSimpleName();
    private static final LLamaAPI INSTANCE = new LLamaAPI();

    // 线程本地状态
    private final ThreadLocal<State> threadLocalState = ThreadLocal.withInitial(() -> State.IDLE);

    // 添加全局状态变量，解决线程间状态同步问题
    private volatile boolean isModelLoaded = false;

    // 记录当前加载的模型名称
    private volatile String currentModelName = null;

    // 执行器服务
    private final ExecutorService executorService;

    // 预测长度
    private final int nlen = 256;

    // 上下文大小，对于大模型可能需要调整
    private final int ctxSize = 2048;

    // 添加消息历史
    private final List<ChatMessage> messageHistory = new ArrayList<>();

    // 添加监听器接口和相关方法
    private final List<ModelStateListener> modelStateListeners = new ArrayList<>();

    public interface ModelStateListener {
        void onModelLoaded();

        void onModelUnloaded();
    }

    public void addModelStateListener(ModelStateListener listener) {
        synchronized (modelStateListeners) {
            if (!modelStateListeners.contains(listener)) {
                modelStateListeners.add(listener);

                // 立即通知当前状态
                if (isModelLoaded) {
                    try {
                        listener.onModelLoaded();
                    } catch (Exception e) {
                        Log.e(TAG, "Error notifying new listener of loaded state", e);
                    }
                }
            }
        }
    }

    public void removeModelStateListener(ModelStateListener listener) {
        synchronized (modelStateListeners) {
            modelStateListeners.remove(listener);
        }
    }

    private void notifyModelLoaded() {
        Log.d(TAG, "Notifying listeners: Model loaded");
        synchronized (modelStateListeners) {
            for (ModelStateListener listener : modelStateListeners) {
                try {
                    listener.onModelLoaded();
                } catch (Exception e) {
                    Log.e(TAG, "Error notifying listener of model load", e);
                }
            }
        }
    }

    private void notifyModelUnloaded() {
        Log.d(TAG, "Notifying listeners: Model unloaded");
        synchronized (modelStateListeners) {
            for (ModelStateListener listener : modelStateListeners) {
                try {
                    listener.onModelUnloaded();
                } catch (Exception e) {
                    Log.e(TAG, "Error notifying listener of model unload", e);
                }
            }
        }
    }

    private LLamaAPI() {
        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "Llm-RunLoop");
                thread.setUncaughtExceptionHandler((t, e) ->
                        Log.e(TAG, "Unhandled exception", e));

                // 初始化本地库
                System.loadLibrary("llama-android");
                log_to_android();
                backend_init(false);

                Log.d(TAG, system_info());

                return thread;
            }
        });
    }

    // 获取单例实例
    public static LLamaAPI getInstance() {
        return INSTANCE;
    }

    // Native 方法声明
    private native void log_to_android();

    private native long load_model(String filename);

    private native void free_model(long model);

    private native long new_context(long model);

    private native void free_context(long context);

    private native void backend_init(boolean numa);

    private native void backend_free();

    private native long new_batch(int nTokens, int embd, int nSeqMax);

    private native void free_batch(long batch);

    private native long new_sampler();

    private native void free_sampler(long sampler);

    private native String bench_model(long context, long model, long batch,
                                      int pp, int tg, int pl, int nr);

    private native String system_info();

    private native int completion_init(long context, long batch, String text,
                                       boolean formatChat, int nLen);

    private native String completion_loop(long context, long batch, long sampler,
                                          int nLen, IntVar ncur);

    private native void kv_cache_clear(long context);

    // 新的JNI方法声明
    private native int chat_completion_init(long context, long batch, long model,
                                            List<ChatMessage> messages, int nLen);

    // 清除聊天历史
    public void clearChatHistory() {
        messageHistory.clear();
        Log.i(TAG, "聊天历史已彻底清除");
    }

    // 清理特殊标记
    private String cleanSpecialTokens(String text) {
        if (text == null) {
            return "";
        }

        // 去掉明显的assistant标记
        text = text.replaceAll("\\bassistant\\b", "");
        
        // 移除重复的assistant标记
        text = text.replaceAll("assistant\\s+assistant", "");
        
        // 移除常见的特殊标记
        text = text.replaceAll("<\\|.*?\\|>", "");
        text = text.replaceAll("</s>|<s>", "");
        
        // 移除角色标记
        text = text.replaceAll("(?i)(user|assistant|system)\\s*:", "");
        
        // 移除连续空白
        text = text.replaceAll("\\s+", " ").trim();
        
        return text;
    }

    // 加载模型
    public void loadModel(String pathToModel) throws IllegalStateException {
        // 记录模型文件名
        final String modelFileName = new File(pathToModel).getName();

        executorService.execute(() -> {
            try {
                if (isModelLoaded || threadLocalState.get() != State.IDLE) {
                    // 如果模型已经加载，通知监听器但不抛出异常
                    Log.i(TAG, "Model already loaded: " + currentModelName);
                    isModelLoaded = true;
                    notifyModelLoaded();
                    return;
                }

                // 开始加载前先清理内存
                System.gc();

                Log.i(TAG, "Starting to load model: " + modelFileName);
                long model = load_model(pathToModel);
                if (model == 0L) {
                    throw new IllegalStateException("load_model() failed");
                }

                // 根据模型大小动态调整线程数
                int threadCount = Runtime.getRuntime().availableProcessors() - 1;
                threadCount = Math.max(1, Math.min(threadCount, 4)); // 保证在1-4之间
                Log.i(TAG, "Using " + threadCount + " threads for model inference");

                long context = new_context(model);
                if (context == 0L) {
                    // 清理模型资源
                    free_model(model);
                    throw new IllegalStateException("new_context() failed");
                }

                long batch = new_batch(512, 0, 1);
                if (batch == 0L) {
                    // 清理已分配的资源
                    free_context(context);
                    free_model(model);
                    throw new IllegalStateException("new_batch() failed");
                }

                long sampler = new_sampler();
                if (sampler == 0L) {
                    // 清理已分配的资源
                    free_batch(batch);
                    free_context(context);
                    free_model(model);
                    throw new IllegalStateException("new_sampler() failed");
                }

                // 更新当前模型名称
                currentModelName = modelFileName;
                Log.i(TAG, "Successfully loaded model: " + currentModelName);

                threadLocalState.set(new State.Loaded(model, context, batch, sampler));

                // 设置全局状态
                isModelLoaded = true;

                // 通知监听器模型已加载
                notifyModelLoaded();

                // 再次清理内存
                System.gc();
            } catch (Exception e) {
                Log.e(TAG, "Error loading model: " + e.getMessage(), e);

                // 确保状态一致
                isModelLoaded = false;
                currentModelName = null;
                threadLocalState.set(State.IDLE);

                // 清理内存
                System.gc();

                // 不向外抛出异常，只是记录，避免崩溃
            }
        });
    }

    // 添加聊天方法
    public void chat(String userMessage, CompletionCallback callback) {
        if (!isModelLoaded) {
            callback.onError(new IllegalStateException("No model loaded"));
            return;
        }
        
        executorService.execute(() -> {
            try {
                State currentState = threadLocalState.get();
                if (!(currentState instanceof State.Loaded)) {
                    throw new IllegalStateException("No model loaded");
                }

                State.Loaded loadedState = (State.Loaded) currentState;
                
                // 添加用户问题到消息历史
                if (messageHistory.size() > 20) { // 增加历史记录长度上限，支持更长对话
                    messageHistory.subList(0, 4).clear(); // 移除最旧的两组对话（4条消息）
                    Log.i(TAG, "移除旧对话，保持历史记录在合理长度");
                }
                messageHistory.add(new ChatMessage("user", userMessage));
                
                // 使用较短的最大响应长度，减少生成过长回复的可能性
                int maxResponseTokens = 250;
                
                // 使用完整的消息历史
                IntVar ncur = new IntVar(chat_completion_init(
                        loadedState.context,
                        loadedState.batch,
                        loadedState.model,
                        messageHistory,
                        maxResponseTokens));
                
                // 用于收集完整响应
                StringBuilder response = new StringBuilder();
                int maxIterations = maxResponseTokens * 2; // 安全上限
                int iterations = 0;

                // 生成循环
                while (iterations < maxIterations) {
                    String token = completion_loop(loadedState.context, loadedState.batch,
                            loadedState.sampler, maxResponseTokens, ncur);
                    
                    // 检查是否结束生成
                    if (token == null) {
                        Log.d(TAG, "EOG标记检测或达到长度上限，终止生成");
                        break;
                    }
                    
                    // 简单过滤特殊标记
                    if (token.contains("<|im_") || token.contains("</s>") || token.contains("<s>")) {
                        continue; // 跳过此token不发送给用户
                    }
                    
                    // 添加到响应并发送给客户端
                    response.append(token);
                    
                    // 直接发送token给客户端
                    String cleanToken = token;
                    if (!cleanToken.isEmpty()) {
                        callback.onToken(cleanToken);
                    }
                    
                    iterations++;
                }
                
                // 获取完整回复并清理
                String finalResponse = cleanSpecialTokens(response.toString());
                
                // 如果回复为空，使用默认回复
                if (finalResponse.trim().isEmpty()) {
                    finalResponse = "我没能理解您的问题，请再说一次。";
                    callback.onToken(finalResponse);
                }
                
                // 记录AI回复到历史中
                messageHistory.add(new ChatMessage("assistant", finalResponse));
                Log.i(TAG, "对话已添加到历史，当前历史长度: " + messageHistory.size());
                
                // 只在真正需要时清理KV缓存
                int estimatedTokens = estimateHistoryTokens();
                if (estimatedTokens > ctxSize * 0.8) { // 如果使用了超过80%的上下文长度
                    Log.i(TAG, "历史接近上下文限制，清理KV缓存");
                    kv_cache_clear(loadedState.context);
                }
                
                callback.onComplete();
            } catch (Exception e) {
                Log.e(TAG, "聊天过程中出错: " + e.getMessage(), e);
                callback.onError(e);
            }
        });
    }

    // 生成文本
    /*
     *  回调模式设计 处理需要长时间运行的文本生成任务
     *
     * */

    // 回调接口
    public interface CompletionCallback {
        void onToken(String token);     // 逐个返回生成的文本片段，流式输出

        void onComplete();              // 任务完成时发出通知

        void onError(Exception e);      // 出错时传递异常
    }

    public void generateCompletion(String message, boolean formatChat, CompletionCallback callback) {
        if (!isModelLoaded) {
            callback.onError(new IllegalStateException("No model loaded"));
            return;
        }

        if (formatChat) {
            // 如果需要聊天格式，直接调用chat方法
            chat(message, callback);
        } else {
            executorService.execute(() -> {
                try {
                    State currentState = threadLocalState.get();
                    if (!(currentState instanceof State.Loaded)) {
                        throw new IllegalStateException("No model loaded");
                    }

                    State.Loaded loadedState = (State.Loaded) currentState;

                    // 在开始新的文本生成前清除KV缓存
                    kv_cache_clear(loadedState.context);

                    IntVar ncur = new IntVar(completion_init(loadedState.context,
                            loadedState.batch, message, false, nlen));

                    while (ncur.getValue() <= nlen) {
                        String str = completion_loop(loadedState.context, loadedState.batch,
                                loadedState.sampler, nlen, ncur);
                        if (str == null) {
                            break;
                        }
                        callback.onToken(str);
                    }

                    kv_cache_clear(loadedState.context);
                    callback.onComplete();
                } catch (Exception e) {
                    callback.onError(e);
                }
            });
        }
    }

    // 添加重置聊天会话的方法
    public void resetChatSession() {
        resetChatSession(true);
    }

    public void resetChatSession(boolean clearHistory) {
        executorService.execute(() -> {
            try {
                if (clearHistory) {
                    // 清空历史记录
                    messageHistory.clear();
                    Log.i(TAG, "已清空聊天历史记录");
                } else {
                    Log.i(TAG, "保留聊天历史，仅重置KV缓存");
                }
                
                State currentState = threadLocalState.get();
                if (currentState instanceof State.Loaded) {
                    State.Loaded loadedState = (State.Loaded) currentState;
                    
                    // 彻底清除KV缓存
                    kv_cache_clear(loadedState.context);
                    
                    Log.i(TAG, "聊天会话已重置，KV缓存已清除");
                }
            } catch (Exception e) {
                Log.e(TAG, "重置聊天会话时出错", e);
            }
        });
    }

    // 卸载模型
    public void unloadModel() {
        executorService.execute(() -> {
            State currentState = threadLocalState.get();
            if (currentState instanceof State.Loaded) {
                State.Loaded loadedState = (State.Loaded) currentState;
                try {
                    Log.i(TAG, "Unloading model: " + currentModelName);

                    // 设置状态
                    threadLocalState.set(State.IDLE);
                    isModelLoaded = false;
                    currentModelName = null;

                    free_context(loadedState.context);
                    free_model(loadedState.model);
                    free_batch(loadedState.batch);
                    free_sampler(loadedState.sampler);

                    // 通知监听器模型已卸载
                    notifyModelUnloaded();

                    // 清理内存
                    System.gc();

                    Log.i(TAG, "Model unloaded successfully");
                } catch (Exception e) {
                    Log.e(TAG, "Error unloading model", e);
                }
            } else {
                // 确保状态一致
                isModelLoaded = false;
                currentModelName = null;
                threadLocalState.set(State.IDLE);

                // 通知监听器
                notifyModelUnloaded();
            }
        });
    }

    // 模型性能测试
    public void benchModel(int pp, int tg, int pl, int nr, BenchCallback callback) {
        executorService.execute(() -> {
            try {
                State currentState = threadLocalState.get();
                if (!(currentState instanceof State.Loaded)) {
                    throw new IllegalStateException("No model loaded");
                }

                State.Loaded loadedState = (State.Loaded) currentState;
                String result = bench_model(loadedState.context, loadedState.model,
                        loadedState.batch, pp, tg, pl, nr);
                callback.onComplete(result);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public interface BenchCallback {
        void onComplete(String result);

        void onError(Exception e);
    }

    // 内部类定义
    /*
     *  线程安全计数器封装
     *  AtomicInteger   保证多线程环境下对整数的操作（如递增、读取）是原子性的
     *
     *  Method:
     *       对外提供简单的 getValue() 和 inc() 方法
     * */
    private static class IntVar {
        private final AtomicInteger value;

        IntVar(int initialValue) {
            this.value = new AtomicInteger(initialValue);
        }

        public int getValue() {
            return value.get();
        }

        public void inc() {
            value.incrementAndGet();
        }
    }

    // 状态类定义
    private static abstract class State {
        static final State IDLE = new Idle();

        private static class Idle extends State {
        }

        static class Loaded extends State {
            final long model;
            final long context;
            final long batch;
            final long sampler;

            Loaded(long model, long context, long batch, long sampler) {
                this.model = model;
                this.context = context;
                this.batch = batch;
                this.sampler = sampler;
            }
        }
    }

    // 判断模型是否已加载
    public boolean isModelLoaded() {
        return isModelLoaded;
    }

    // 保存聊天记录
    public static class ChatMessage {
        public final String role;
        public final String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    // 获取当前加载的模型名称
    public String getCurrentModelName() {
        return currentModelName;
    }

    // 添加获取当前对话历史长度的方法
    public int getChatHistorySize() {
        return messageHistory.size();
    }

    // 添加一个方法在界面显示当前对话状态
    public String getChatHistorySummary() {
        if (messageHistory.isEmpty()) {
            return "当前没有对话历史";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("当前对话历史：").append(messageHistory.size()).append("条消息\n");
        
        // 可以选择性地显示最近几轮对话的简短摘要
        int recentCount = Math.min(4, messageHistory.size());
        if (recentCount > 0) {
            summary.append("最近的消息：\n");
            for (int i = messageHistory.size() - recentCount; i < messageHistory.size(); i++) {
                ChatMessage msg = messageHistory.get(i);
                String role = "user".equals(msg.role) ? "用户" : "AI";
                String content = msg.content;
                if (content.length() > 20) {
                    content = content.substring(0, 20) + "...";
                }
                summary.append(role).append(": ").append(content).append("\n");
            }
        }
        
        return summary.toString();
    }

    // 估算历史消息的token数量
    private int estimateHistoryTokens() {
        // 简单估算：每个字符约0.5个token
        int estimate = 0;
        for (ChatMessage msg : messageHistory) {
            estimate += (msg.content.length() / 2) + 10; // 10是角色标记的估计token数
        }
        return estimate;
    }
}