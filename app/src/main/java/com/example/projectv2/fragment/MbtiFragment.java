package com.example.projectv2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectv2.R;
import com.example.projectv2.api.ApiClient;
import com.example.projectv2.model.MbtiQuestion;
import com.example.projectv2.model.MbtiType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MbtiFragment extends Fragment {
    private List<MbtiQuestion> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private Map<String, Integer> dimensionScores = new HashMap<>();

    private LinearLayout questionLayout;
    private ScrollView resultLayout;
    private TextView questionProgress;
    private TextView questionText;
    private RadioGroup optionsGroup;
    private RadioButton optionA;
    private RadioButton optionB;
    private Button nextButton;
    private Button retestButton;
    private TextView mbtiTypeText;
    private TextView typeNameText;
    private TextView descriptionText;
    private TextView characteristicsText;
    private TextView strengthsText;
    private TextView weaknessesText;

    public static MbtiFragment newInstance() {
        return new MbtiFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mbti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        loadQuestions();
    }

    private void initViews(View view) {
        questionLayout = view.findViewById(R.id.questionLayout);
        resultLayout = view.findViewById(R.id.resultLayout);
        questionProgress = view.findViewById(R.id.questionProgress);
        questionText = view.findViewById(R.id.questionText);
        optionsGroup = view.findViewById(R.id.optionsGroup);
        optionA = view.findViewById(R.id.optionA);
        optionB = view.findViewById(R.id.optionB);
        nextButton = view.findViewById(R.id.nextButton);
        retestButton = view.findViewById(R.id.retestButton);
        mbtiTypeText = view.findViewById(R.id.mbtiTypeText);
        typeNameText = view.findViewById(R.id.typeNameText);
        descriptionText = view.findViewById(R.id.descriptionText);
        characteristicsText = view.findViewById(R.id.characteristicsText);
        strengthsText = view.findViewById(R.id.strengthsText);
        weaknessesText = view.findViewById(R.id.weaknessesText);

        nextButton.setOnClickListener(v -> handleNextQuestion());
        retestButton.setOnClickListener(v -> restartTest());

        // 初始化维度分数
        dimensionScores.put("EI", 0);
        dimensionScores.put("SN", 0);
        dimensionScores.put("TF", 0);
        dimensionScores.put("JP", 0);
    }

    private void loadQuestions() {
        ApiClient.getUserApi().getMbtiQuestions().enqueue(new Callback<List<MbtiQuestion>>() {
            @Override
            public void onResponse(Call<List<MbtiQuestion>> call, Response<List<MbtiQuestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body();
                    showQuestion(0);
                }
            }

            @Override
            public void onFailure(Call<List<MbtiQuestion>> call, Throwable t) {
                Toast.makeText(getContext(), "加载问题失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion(int index) {
        if (index < questions.size()) {
            MbtiQuestion question = questions.get(index);
            questionProgress.setText((index + 1) + "/" + questions.size());
            questionText.setText(question.getQuestionText());
            optionA.setText(question.getOptionA());
            optionB.setText(question.getOptionB());
            optionsGroup.clearCheck();
        }
    }

    private void handleNextQuestion() {
        if (optionsGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "请选择一个选项", Toast.LENGTH_SHORT).show();
            return;
        }

        // 记录答案
        MbtiQuestion currentQuestion = questions.get(currentQuestionIndex);
        boolean choseOptionA = optionA.isChecked();
        String dimension = currentQuestion.getDimension();
        
        // 更新维度分数
        int currentScore = dimensionScores.get(dimension);
        dimensionScores.put(dimension, choseOptionA ? currentScore + 1 : currentScore);

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion(currentQuestionIndex);
        } else {
            calculateAndShowResult();
        }
    }

    private void calculateAndShowResult() {
        final String mbtiType = ""
                + (dimensionScores.get("EI") >= 3 ? "E" : "I")
                + (dimensionScores.get("SN") >= 3 ? "S" : "N")
                + (dimensionScores.get("TF") >= 3 ? "T" : "F")
                + (dimensionScores.get("JP") >= 3 ? "J" : "P");

        // 获取MBTI类型描述
        ApiClient.getUserApi().getMbtiType(mbtiType).enqueue(new Callback<MbtiType>() {
            @Override
            public void onResponse(Call<MbtiType> call, Response<MbtiType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showResult(response.body());
                    // 更新用户的MBTI类型
                    updateUserMbtiType(mbtiType);
                }
            }

            @Override
            public void onFailure(Call<MbtiType> call, Throwable t) {
                Toast.makeText(getContext(), "获取结果失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResult(MbtiType mbtiType) {
        questionLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);

        mbtiTypeText.setText(mbtiType.getTypeCode());
        typeNameText.setText(mbtiType.getTypeName());
        descriptionText.setText(mbtiType.getDescription());
        characteristicsText.setText(mbtiType.getCharacteristics());
        strengthsText.setText(mbtiType.getStrengths());
        weaknessesText.setText(mbtiType.getWeaknesses());
    }

    private void updateUserMbtiType(String mbtiType) {
        // TODO: 获取当前用户ID
        Long userId = 1L; // 这里需要从SharedPreferences或其他地方获取当前用户ID
        
        // 创建请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("mbtiType", mbtiType);
        
        ApiClient.getUserApi().updateUserMbtiType(userId, requestBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "MBTI类型更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "更新MBTI类型失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "更新MBTI类型失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restartTest() {
        currentQuestionIndex = 0;
        dimensionScores.put("EI", 0);
        dimensionScores.put("SN", 0);
        dimensionScores.put("TF", 0);
        dimensionScores.put("JP", 0);
        
        questionLayout.setVisibility(View.VISIBLE);
        resultLayout.setVisibility(View.GONE);
        
        showQuestion(0);
    }
} 