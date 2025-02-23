package com.example.projectv2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectv2.R;
import com.example.projectv2.adapter.MessageAdapter;
import com.example.projectv2.db.ChatDbHelper;
import com.example.projectv2.model.Message;

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
            // 保存并显示用户消息
            Message userMessage = new Message(content, false);
            dbHelper.insertMessage(userMessage);
            messageAdapter.addMessage(userMessage);

            // 清空输入框
            messageInput.setText("");

            // 滚动到底部
            messagesRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

            // 模拟AI回复
            Message aiMessage = new Message("你好！世界", true);
            dbHelper.insertMessage(aiMessage);
            messageAdapter.addMessage(aiMessage);

            // 滚动到底部
            messagesRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
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