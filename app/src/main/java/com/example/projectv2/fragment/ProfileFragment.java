package com.example.projectv2.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectv2.LLamaAPI;
import com.example.projectv2.LoginActivity;
import com.example.projectv2.R;
import com.example.projectv2.api.ApiClient;
import com.example.projectv2.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProfileFragment extends Fragment implements LLamaAPI.ModelStateListener {
    private static final String TAG = "ProfileFragment";
    
    private CardView avatarContainer;
    private ImageView avatarImage;
    private TextView usernameText;
    private TextView mbtiTypeText;
    private TextView bioText;
    private TextView gradeText;
    private TextView genderText;
    private TextView ageText;
    private View gradeContainer;
    private View genderContainer;
    private View ageContainer;
    private TextView changePasswordButton;
    private TextView logoutButton;

    // 添加模型管理相关UI元素
    private CardView modelManagementCard;
    private TextView modelStatusText;
    private Button loadModelButton;
    private Button unloadModelButton;
    private ProgressBar modelLoadingProgress;
    
    private Long userId;
    private User currentUser;
    private LLamaAPI llamaApi;
    private boolean isModelLoading = false;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST = 2;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners();
        loadUserInfo();
        
        // 初始化LLamaAPI
        llamaApi = LLamaAPI.getInstance();
        
        // 检查模型状态
        updateModelStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uploadImage(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else if (isAdded() && getContext() != null) {
                Toast.makeText(requireContext(), "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initViews(View view) {
        avatarContainer = view.findViewById(R.id.avatarContainer);
        avatarImage = view.findViewById(R.id.avatarImage);
        usernameText = view.findViewById(R.id.usernameText);
        mbtiTypeText = view.findViewById(R.id.mbtiTypeText);
        bioText = view.findViewById(R.id.bioText);
        gradeText = view.findViewById(R.id.gradeText);
        genderText = view.findViewById(R.id.genderText);
        ageText = view.findViewById(R.id.ageText);
        gradeContainer = view.findViewById(R.id.gradeContainer);
        genderContainer = view.findViewById(R.id.genderContainer);
        ageContainer = view.findViewById(R.id.ageContainer);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // 初始化模型管理相关UI元素
        modelManagementCard = view.findViewById(R.id.modelManagementCard);
        modelStatusText = view.findViewById(R.id.modelStatusText);
        loadModelButton = view.findViewById(R.id.loadModelButton);
        unloadModelButton = view.findViewById(R.id.unloadModelButton);
        modelLoadingProgress = view.findViewById(R.id.modelLoadingProgress);

        // 从SharedPreferences获取用户ID
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userId = prefs.getLong("user_id", -1);
    }

    private void setupListeners() {
        avatarContainer.setOnClickListener(v -> showChangeAvatarDialog());
        usernameText.setOnClickListener(v -> showEditUsernameDialog());
        bioText.setOnClickListener(v -> showEditBioDialog());
        gradeContainer.setOnClickListener(v -> showEditGradeDialog());
        genderContainer.setOnClickListener(v -> showEditGenderDialog());
        ageContainer.setOnClickListener(v -> showEditAgeDialog());
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());
        logoutButton.setOnClickListener(v -> showLogoutConfirmDialog());
        
        // 设置模型管理相关监听器
        loadModelButton.setOnClickListener(v -> loadModel());
        unloadModelButton.setOnClickListener(v -> unloadModel());
    }

    private void loadUserInfo() {
        if (userId == -1) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        ApiClient.getUserApi().getUserInfo(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (isAdded()) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUser = response.body();
                        updateUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "获取用户信息失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI() {
        if (currentUser == null) return;

        usernameText.setText(currentUser.getUsername());
        mbtiTypeText.setText(currentUser.getMbtiType() != null ? currentUser.getMbtiType() : "未完成MBTI测试");
        bioText.setText(currentUser.getBio() != null ? currentUser.getBio() : "点击添加个性签名");
        gradeText.setText(currentUser.getGrade() != null ? currentUser.getGrade() : "未设置");
        genderText.setText(currentUser.getGender() != null ? currentUser.getGender() : "未设置");
        ageText.setText(currentUser.getAge() != null ? String.valueOf(currentUser.getAge()) : "未设置");

        // 加载头像
        if (currentUser.getAvatarUrl() != null) {
            Glide.with(this)
                    .load(ApiClient.BASE_URL.substring(0, ApiClient.BASE_URL.length() - 1) + currentUser.getAvatarUrl())
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(avatarImage);
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10及以上版本不需要存储权限就可以访问媒体文件
            openImagePicker();
        } else {
            // Android 9及以下版本需要请求存储权限
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST);
            } else {
                openImagePicker();
            }
        }
    }

    private void showChangeAvatarDialog() {
        String[] options = {"从相册选择", "取消"};
        new AlertDialog.Builder(requireContext())
                .setTitle("更换头像")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        checkStoragePermission();
                    }
                })
                .show();
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(Uri imageUri) {
        try {
            final String mimeType = requireContext().getContentResolver().getType(imageUri);
            final String finalMimeType = mimeType != null ? mimeType : "image/*";

            // 创建请求体
            RequestBody requestFile = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse(finalMimeType);
                }

                @Override
                public void writeTo(okio.BufferedSink sink) throws IOException {
                    InputStream inputStream = null;
                    try {
                        inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                        if (inputStream == null) {
                            throw new IOException("无法打开文件流");
                        }
                        byte[] buffer = new byte[4096];
                        int read;
                        while ((read = inputStream.read(buffer)) != -1) {
                            sink.write(buffer, 0, read);
                        }
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                }
            };

            // 创建 MultipartBody.Part
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(this.userId));

            // 发送请求
            ApiClient.getUserApi().uploadAvatar(body, userId).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (isAdded() && getContext() != null) {
                        if (response.isSuccessful() && response.body() != null) {
                            String avatarUrl = response.body().get("url");
                            // 更新UI显示新头像
                            Glide.with(ProfileFragment.this)
                                    .load(ApiClient.BASE_URL.substring(0, ApiClient.BASE_URL.length() - 1) + avatarUrl)
                                    .placeholder(R.drawable.default_avatar)
                                    .error(R.drawable.default_avatar)
                                    .into(avatarImage);
                            Toast.makeText(getContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    if (isAdded() && getContext() != null) {
                        Toast.makeText(getContext(), "头像上传失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), "文件处理失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEditUsernameDialog() {
        if (!isAdded() || getContext() == null) return;
        
        EditText input = new EditText(getContext());
        input.setText(currentUser.getUsername());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setSingleLine(true);

        new AlertDialog.Builder(getContext())
                .setTitle("修改用户名")
                .setView(input)
                .setPositiveButton("确定", (dialog, which) -> {
                    String newUsername = input.getText().toString().trim();
                    if (newUsername.isEmpty()) {
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    if (newUsername.equals(currentUser.getUsername())) {
                        return;
                    }
                    updateUserField("username", newUsername);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showEditBioDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("编辑个性签名");

        final EditText input = new EditText(requireContext());
        input.setText(currentUser.getBio());
        builder.setView(input);

        builder.setPositiveButton("确定", (dialog, which) -> {
            String newBio = input.getText().toString().trim();
            updateUserField("bio", newBio);
        });
        builder.setNegativeButton("取消", null);

        builder.show();
    }

    private void showEditGradeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("选择年级");

        final String[] grades = {"大一", "大二", "大三", "大四", "研究生"};
        builder.setItems(grades, (dialog, which) -> {
            String selectedGrade = grades[which];
            updateUserField("grade", selectedGrade);
        });

        builder.show();
    }

    private void showEditGenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("选择性别");

        final String[] genders = {"男", "女", "其他"};
        builder.setItems(genders, (dialog, which) -> {
            String selectedGender = genders[which];
            updateUserField("gender", selectedGender);
        });

        builder.show();
    }

    private void showEditAgeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("编辑年龄");

        final EditText input = new EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        if (currentUser.getAge() != null) {
            input.setText(String.valueOf(currentUser.getAge()));
        }
        builder.setView(input);

        builder.setPositiveButton("确定", (dialog, which) -> {
            try {
                int newAge = Integer.parseInt(input.getText().toString().trim());
                if (newAge > 0 && newAge < 150) {
                    updateUserField("age", String.valueOf(newAge));
                } else {
                    Toast.makeText(getContext(), "请输入有效年龄", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效数字", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", null);

        builder.show();
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null);
        EditText oldPasswordInput = dialogView.findViewById(R.id.oldPasswordInput);
        EditText newPasswordInput = dialogView.findViewById(R.id.newPasswordInput);
        EditText confirmPasswordInput = dialogView.findViewById(R.id.confirmPasswordInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("修改密码")
                .setView(dialogView)
                .setPositiveButton("确定", (dialog, which) -> {
                    String oldPassword = oldPasswordInput.getText().toString().trim();
                    String newPassword = newPasswordInput.getText().toString().trim();
                    String confirmPassword = confirmPasswordInput.getText().toString().trim();

                    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(getContext(), "请填写所有字段", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        Toast.makeText(getContext(), "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updatePassword(oldPassword, newPassword);
                })
                .setNegativeButton("取消", null);

        builder.show();
    }

    private void showLogoutConfirmDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> logout())
                .setNegativeButton("取消", null)
                .show();
    }

    private void updateUserField(String field, String value) {
        Map<String, String> updateData = new HashMap<>();
        updateData.put(field, value);

        ApiClient.getUserApi().updateUserField(userId, updateData).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUser = response.body();
                        updateUI();
                        Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "更新失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword) {
        Map<String, String> passwordData = new HashMap<>();
        passwordData.put("oldPassword", oldPassword);
        passwordData.put("newPassword", newPassword);

        ApiClient.getUserApi().updatePassword(userId, passwordData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "密码修改失败，请检查原密码是否正确", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "密码修改失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logout() {
        // 清除SharedPreferences中的用户信息
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        // 跳转到登录页面
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    // 添加模型管理相关方法
    private void loadModel() {
        if (isModelLoading) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), "模型正在加载中，请稍候...", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        
        // 显示模型选择对话框
        if (isAdded() && getContext() != null) {
            String[] models = {"小模型(QwQ-0.5B，快速)", "大模型(Minicpm-4B，高质量)"};
            new AlertDialog.Builder(getContext())
                .setTitle("选择要加载的模型")
                .setItems(models, (dialog, which) -> {
                    String modelFileName = which == 0 ? "QwQ-0.5B.Q4_K_M.gguf" : "Minicpm-4B-Q4_K_M.gguf";
                    startModelLoading(modelFileName);
                })
                .show();
        }
    }
    
    private void startModelLoading(String modelFileName) {
        // 显示加载进度条
        isModelLoading = true;
        modelLoadingProgress.setVisibility(View.VISIBLE);
        modelStatusText.setText("正在加载模型...");
        loadModelButton.setEnabled(false);
        unloadModelButton.setEnabled(false);
        
        // 在后台线程中加载模型
        new Thread(() -> {
            try {
                // 从assets复制模型到外部存储
                String modelPath = copyAssetToExternalStorage(modelFileName);
                if (modelPath == null) {
                    throw new IOException("无法复制模型文件");
                }
                
                Log.d(TAG, "开始加载模型: " + modelFileName);
                // 加载模型 (监听器将处理成功加载后的UI更新)
                llamaApi.loadModel(modelPath);
            } catch (Exception e) {
                Log.e(TAG, "模型加载失败", e);
                // 主线程更新UI
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (isAdded() && getActivity() != null) {
                        isModelLoading = false;
                        modelLoadingProgress.setVisibility(View.GONE);
                        modelStatusText.setText("模型加载失败: " + e.getMessage());
                        loadModelButton.setEnabled(true);
                        unloadModelButton.setEnabled(false);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "模型加载失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    
    private void unloadModel() {
        modelStatusText.setText("正在卸载模型...");
        loadModelButton.setEnabled(false);
        unloadModelButton.setEnabled(false);
        
        // 卸载模型 (监听器将处理卸载后的UI更新)
        llamaApi.unloadModel();
    }
    
    private void updateModelStatus() {
        try {
            // 检查模型是否已加载
            boolean isModelLoaded = llamaApi != null && llamaApi.isModelLoaded();
            String currentModelName = llamaApi != null ? llamaApi.getCurrentModelName() : null;
            
            Log.d(TAG, "updateModelStatus: isModelLoaded = " + isModelLoaded + 
                  ", isModelLoading = " + isModelLoading + 
                  ", currentModel = " + currentModelName);
            
            modelLoadingProgress.setVisibility(View.GONE);
            loadModelButton.setEnabled(!isModelLoaded && !isModelLoading);
            unloadModelButton.setEnabled(isModelLoaded && !isModelLoading);
            
            if (isModelLoaded) {
                if (currentModelName != null) {
                    // 显示模型名称
                    if (currentModelName.contains("QwQ")) {
                        modelStatusText.setText("已加载小模型 (QwQ-0.5B)，可以开始聊天");
                    } else if (currentModelName.contains("Minicpm")) {
                        modelStatusText.setText("已加载大模型 (Minicpm-4B)，可以开始聊天");
                    } else {
                        modelStatusText.setText("模型已加载: " + currentModelName);
                    }
                } else {
                    modelStatusText.setText("模型已加载，可以开始聊天");
                }
            } else if (isModelLoading) {
                modelStatusText.setText("模型加载中...");
                modelLoadingProgress.setVisibility(View.VISIBLE);
            } else {
                modelStatusText.setText("模型未加载，请先加载模型");
            }
        } catch (Exception e) {
            Log.e(TAG, "更新模型状态失败", e);
            modelStatusText.setText("无法获取模型状态");
        }
    }
    
    private String copyAssetToExternalStorage(String assetName) {
        if (!isAdded() || getContext() == null) return null;
        
        File outputFile = new File(getContext().getExternalFilesDir(null), assetName);
        
        // 如果文件已存在，直接返回路径
        if (outputFile.exists() && outputFile.length() > 0) {
            Log.i(TAG, "模型文件已存在: " + outputFile.getAbsolutePath());
            return outputFile.getAbsolutePath();
        }
        
        // 否则从assets复制
        try (InputStream in = getContext().getAssets().open(assetName);
             OutputStream out = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[8192];
            int read;
            long total = 0;
            long fileSize = in.available();
            
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                total += read;
                
                // 更新进度（可选）
                int finalProgress = (int) (total * 100 / fileSize);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (isAdded()) {
                        modelLoadingProgress.setProgress(finalProgress);
                        modelStatusText.setText("正在加载模型... " + finalProgress + "%");
                    }
                });
            }
            
            Log.i(TAG, "模型文件复制完成: " + outputFile.getAbsolutePath());
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "复制模型文件失败", e);
            return null;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // 注册监听器
        if (llamaApi != null) {
            llamaApi.addModelStateListener(this);
        }
        
        // 更新状态
        updateModelStatus();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        // 移除监听器
        if (llamaApi != null) {
            llamaApi.removeModelStateListener(this);
        }
    }
    
    // 实现ModelStateListener接口
    @Override
    public void onModelLoaded() {
        // 在主线程更新UI
        if (isAdded() && getActivity() != null) {
            Log.d(TAG, "onModelLoaded callback received");
            getActivity().runOnUiThread(() -> {
                isModelLoading = false;
                updateModelStatus();
                if (getContext() != null) {
                    Toast.makeText(getContext(), "模型已加载", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    @Override
    public void onModelUnloaded() {
        // 在主线程更新UI
        if (isAdded() && getActivity() != null) {
            Log.d(TAG, "onModelUnloaded callback received");
            getActivity().runOnUiThread(() -> {
                updateModelStatus();
                if (getContext() != null) {
                    Toast.makeText(getContext(), "模型已卸载", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
} 