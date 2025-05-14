package com.example.mental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "scl90_results")
public class SCL90Result {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "total_score", nullable = false)
    private int totalScore;
    
    @Column(name = "total_average", nullable = false)
    private double totalAverage;
    
    @Column(name = "positive_items", nullable = false)
    private int positiveItems;
    
    @Column(name = "positive_average", nullable = false)
    private double positiveAverage;
    
    @Column(name = "factor_scores", columnDefinition = "TEXT")
    @JsonIgnore // 不序列化此字段，而是使用factorScores
    private String factorScoresJson; // 存储为JSON字符串
    
    @Transient // 不持久化此字段
    private Map<String, Double> factorScores; // 运行时使用的因子分数
    
    // Getters and setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getTotalAverage() {
        return totalAverage;
    }

    public void setTotalAverage(double totalAverage) {
        this.totalAverage = totalAverage;
    }

    public int getPositiveItems() {
        return positiveItems;
    }

    public void setPositiveItems(int positiveItems) {
        this.positiveItems = positiveItems;
    }

    public double getPositiveAverage() {
        return positiveAverage;
    }

    public void setPositiveAverage(double positiveAverage) {
        this.positiveAverage = positiveAverage;
    }

    public String getFactorScoresJson() {
        return factorScoresJson;
    }

    public void setFactorScoresJson(String factorScoresJson) {
        this.factorScoresJson = factorScoresJson;
        // 同时更新factorScores字段
        if (factorScoresJson != null && !factorScoresJson.isEmpty()) {
            try {
                this.factorScores = objectMapper.readValue(factorScoresJson, new TypeReference<Map<String, Double>>() {});
            } catch (JsonProcessingException e) {
                this.factorScores = new HashMap<>();
            }
        } else {
            this.factorScores = new HashMap<>();
        }
    }
    
    public Map<String, Double> getFactorScores() {
        // 如果factorScores为null但factorScoresJson不为null，则尝试解析
        if (factorScores == null && factorScoresJson != null && !factorScoresJson.isEmpty()) {
            try {
                factorScores = objectMapper.readValue(factorScoresJson, new TypeReference<Map<String, Double>>() {});
            } catch (JsonProcessingException e) {
                factorScores = new HashMap<>();
            }
        }
        return factorScores;
    }
    
    public void setFactorScores(Map<String, Double> factorScores) {
        this.factorScores = factorScores;
        // 同时更新factorScoresJson字段
        if (factorScores != null) {
            try {
                this.factorScoresJson = objectMapper.writeValueAsString(factorScores);
            } catch (JsonProcessingException e) {
                this.factorScoresJson = "{}";
            }
        } else {
            this.factorScoresJson = "{}";
        }
    }
} 