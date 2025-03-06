package com.example.projectv2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectv2.R;
import com.example.projectv2.adapter.MessageAdapter;
import com.example.projectv2.db.ChatDbHelper;
import com.example.projectv2.model.Message;
import com.example.projectv2.service.AiService;

import java.util.List;

public class AiChatFragment extends Fragment {
    
    private RecyclerView messagesRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private MessageAdapter messageAdapter;
    private ChatDbHelper dbHelper;

    public static AiChatFragment newInstance() {
        return new AiChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化数据库
        dbHelper = new ChatDbHelper(requireContext());
        
        // 初始化视图
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        // 设置RecyclerView
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Message> messages = dbHelper.getAllMessages();
        messageAdapter = new MessageAdapter(messages);
        messagesRecyclerView.setAdapter(messageAdapter);

        // 设置发送按钮点击事件
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String content = messageInput.getText().toString().trim();
        if (!content.isEmpty()) {
            try {
                // 保存并显示用户消息
                Message userMessage = new Message(content, false);
                dbHelper.insertMessage(userMessage);
                messageAdapter.addMessage(userMessage);

                // 清空输入框
                messageInput.setText("");

                // 滚动到底部
                messagesRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                // 创建AI消息对象
                Message aiMessage = new Message("", true);
                dbHelper.insertMessage(aiMessage);
                messageAdapter.addMessage(aiMessage);

                // 调用AI服务
                AiService.getAiResponse(content, new AiService.AiResponseCallback() {
                    private StringBuilder currentResponse = new StringBuilder();

                    @Override
                    public void onResponse(String response) {
                        if (getActivity() == null) return;
                        
                        getActivity().runOnUiThread(() -> {
                            try {
                                currentResponse.append(response);
                                aiMessage.setContent(currentResponse.toString());
                                messageAdapter.notifyItemChanged(messageAdapter.getItemCount() - 1);
                                messagesRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            } catch (Exception e) {
                                Log.e("AiChatFragment", "Error updating UI", e);
                                Toast.makeText(requireContext(), "更新UI失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        if (getActivity() == null) return;

                        getActivity().runOnUiThread(() -> {
                            try {
                                aiMessage.setContent(currentResponse.toString());
                                dbHelper.insertMessage(aiMessage);
                                messageAdapter.notifyItemChanged(messageAdapter.getItemCount() - 1);
                            } catch (Exception e) {
                                Log.e("AiChatFragment", "Error completing response", e);
                                Toast.makeText(requireContext(), "保存消息失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        if (getActivity() == null) return;

                        Log.e("AiChatFragment", "AI service error: " + error);
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } catch (Exception e) {
                Log.e("AiChatFragment", "Error sending message", e);
                Toast.makeText(requireContext(), "发送消息失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
} 