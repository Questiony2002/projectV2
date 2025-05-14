package com.example.mental.service.impl;

import com.example.mental.dto.SCL90ResultDTO;
import com.example.mental.entity.SCL90Result;
import com.example.mental.repository.SCL90Repository;
import com.example.mental.service.SCL90Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class SCL90ServiceImpl implements SCL90Service {

    @Autowired
    private SCL90Repository scl90Repository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public SCL90Result saveResult(SCL90ResultDTO resultDTO) {
        // 查找是否已有结果
        Optional<SCL90Result> existingResult = scl90Repository.findByUserId(resultDTO.getUserId());
        
        SCL90Result result;
        if (existingResult.isPresent()) {
            result = existingResult.get();
        } else {
            result = new SCL90Result();
            result.setUserId(resultDTO.getUserId());
        }
        
        // 更新结果
        result.setTotalScore(resultDTO.getTotalScore());
        result.setTotalAverage(resultDTO.getTotalAverage());
        result.setPositiveItems(resultDTO.getPositiveItems());
        result.setPositiveAverage(resultDTO.getPositiveAverage());
        
        // 将Map转换为JSON字符串
        try {
            String factorScoresJson = objectMapper.writeValueAsString(resultDTO.getFactorScores());
            result.setFactorScoresJson(factorScoresJson);
        } catch (Exception e) {
            throw new RuntimeException("Error converting factor scores to JSON", e);
        }
        
        return scl90Repository.save(result);
    }

    @Override
    public Optional<SCL90Result> getResultByUserId(Long userId) {
        return scl90Repository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteResultByUserId(Long userId) {
        scl90Repository.deleteByUserId(userId);
    }
} 