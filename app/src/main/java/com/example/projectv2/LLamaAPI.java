package com.example.projectv2;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class LLamaAPI {
    private static final String TAG = LLamaAPI.class.getSimpleName();
    private static final LLamaAPI INSTANCE = new LLamaAPI();

    // 线程本地状态
    private final ThreadLocal<State> threadLocalState = ThreadLocal.withInitial(() -> State.IDLE);

    // 执行器服务
    private final ExecutorService executorService;

    // 预测长度
    private final int nlen = 64;

    // 添加消息历史
    private final List<ChatMessage> messageHistory = new ArrayList<>();

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
    }

    // 加载模型
    public void loadModel(String pathToModel) throws IllegalStateException {
        executorService.execute(() -> {
            if (threadLocalState.get() != State.IDLE) {
                throw new IllegalStateException("Model already loaded");
            }

            long model = load_model(pathToModel);
            if (model == 0L) {
                throw new IllegalStateException("load_model() failed");
            }

            long context = new_context(model);
            if (context == 0L) {
                throw new IllegalStateException("new_context() failed");
            }

            long batch = new_batch(512, 0, 1);
            if (batch == 0L) {
                throw new IllegalStateException("new_batch() failed");
            }

            long sampler = new_sampler();
            if (sampler == 0L) {
                throw new IllegalStateException("new_sampler() failed");
            }

            Log.i(TAG, "Loaded model " + pathToModel);
            threadLocalState.set(new State.Loaded(model, context, batch, sampler));
        });
    }


    // 添加聊天方法
    public void chat(String userMessage, CompletionCallback callback) {
        messageHistory.add(new ChatMessage("user", userMessage));

        executorService.execute(() -> {
            try {
                State currentState = threadLocalState.get();
                if (!(currentState instanceof State.Loaded)) {
                    throw new IllegalStateException("No model loaded");
                }

                State.Loaded loadedState = (State.Loaded) currentState;

                // 调用新的JNI方法处理聊天
                IntVar ncur = new IntVar(chat_completion_init(
                        loadedState.context,
                        loadedState.batch,
                        loadedState.model,
                        messageHistory,
                        nlen));

                StringBuilder responseBuilder = new StringBuilder();
                while (ncur.getValue() <= nlen) {
                    String str = completion_loop(loadedState.context, loadedState.batch,
                            loadedState.sampler, nlen, ncur);
                    if (str == null) {
                        break;
                    }
                    responseBuilder.append(str);
                    callback.onToken(str);
                }

                // 保存AI的回复到历史记录
                messageHistory.add(new ChatMessage("assistant", responseBuilder.toString()));

                kv_cache_clear(loadedState.context);
                callback.onComplete();
            } catch (Exception e) {
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
        if (formatChat) {
            // 如果需要聊天格式，使用新的chat方法
            clearChatHistory(); // 清除之前的历史
            chat(message, callback);
        } else {
            executorService.execute(() -> {
                try {
                    State currentState = threadLocalState.get();
                    if (!(currentState instanceof State.Loaded)) {
                        throw new IllegalStateException("No model loaded");
                    }

                    State.Loaded loadedState = (State.Loaded) currentState;
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

    // 卸载模型
    public void unloadModel() {
        executorService.execute(() -> {
            State currentState = threadLocalState.get();
            if (currentState instanceof State.Loaded) {
                State.Loaded loadedState = (State.Loaded) currentState;
                free_context(loadedState.context);
                free_model(loadedState.model);
                free_batch(loadedState.batch);
                free_sampler(loadedState.sampler);
                threadLocalState.set(State.IDLE);
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

        private static class Idle extends State {}

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

    // 保存聊天记录
    public static class ChatMessage {
        public final String role;
        public final String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}