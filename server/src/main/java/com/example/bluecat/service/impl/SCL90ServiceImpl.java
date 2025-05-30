package com.example.bluecat.service.impl;

import com.example.bluecat.dto.SCL90FactorDTO;
import com.example.bluecat.dto.SCL90QuestionDTO;
import com.example.bluecat.dto.SCL90ResultDTO;
import com.example.bluecat.entity.SCL90Factor;
import com.example.bluecat.entity.SCL90Question;
import com.example.bluecat.entity.SCL90Result;
import com.example.bluecat.mapper.SCL90FactorMapper;
import com.example.bluecat.mapper.SCL90Mapper;
import com.example.bluecat.mapper.SCL90QuestionMapper;
import com.example.bluecat.service.SCL90Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SCL90ServiceImpl implements SCL90Service {

    @Autowired
    private SCL90Mapper scl90Mapper;
    
    @Autowired
    private SCL90QuestionMapper scl90QuestionMapper;
    
    @Autowired
    private SCL90FactorMapper scl90FactorMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public SCL90Result saveResult(SCL90ResultDTO resultDTO) {
        // 查找是否已有结果
        SCL90Result existingResult = scl90Mapper.findByUserId(resultDTO.getUserId());
        
        SCL90Result result;
        if (existingResult != null) {
            result = existingResult;
            // 更新结果
            result.setUserId(resultDTO.getUserId());
            result.setTotalScore(resultDTO.getTotalScore());
            result.setTotalAverage(resultDTO.getTotalAverage());
            result.setPositiveItems(resultDTO.getPositiveItems());
            result.setPositiveAverage(resultDTO.getPositiveAverage());
            try {
                result.setFactorScores(objectMapper.writeValueAsString(resultDTO.getFactorScores()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize factor scores", e);
            }
            scl90Mapper.updateById(result);
        } else {
            result = new SCL90Result();
            result.setUserId(resultDTO.getUserId());
            result.setTotalScore(resultDTO.getTotalScore());
            result.setTotalAverage(resultDTO.getTotalAverage());
            result.setPositiveItems(resultDTO.getPositiveItems());
            result.setPositiveAverage(resultDTO.getPositiveAverage());
            try {
                result.setFactorScores(objectMapper.writeValueAsString(resultDTO.getFactorScores()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize factor scores", e);
            }
            scl90Mapper.insert(result);
        }
        
        return result;
    }

    @Override
    public Optional<SCL90Result> getResultByUserId(Long userId) {
        SCL90Result result = scl90Mapper.findByUserId(userId);
        return Optional.ofNullable(result);
    }

    @Override
    @Transactional
    public void deleteResultByUserId(Long userId) {
        scl90Mapper.deleteByUserId(userId);
    }
    
    @Override
    public List<SCL90QuestionDTO> getAllQuestions() {
        // 从数据库获取问题
        List<SCL90Question> questions = scl90QuestionMapper.findAllQuestions();
        
        // 转换为DTO
        return questions.stream()
                .map(this::convertToQuestionDTO)
                .collect(Collectors.toList());
    }
    
    // 新增：因子相关方法
    @Override
    public List<SCL90FactorDTO> getAllFactors() {
        List<SCL90Factor> factors = scl90FactorMapper.findAllFactors();
        return factors.stream()
                .map(this::convertToFactorDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public SCL90FactorDTO getFactorById(Integer id) {
        SCL90Factor factor = scl90FactorMapper.findById(id);
        return factor != null ? convertToFactorDTO(factor) : null;
    }
    
    @Override
    public SCL90FactorDTO getFactorByName(String factorName) {
        SCL90Factor factor = scl90FactorMapper.findByFactorName(factorName);
        return factor != null ? convertToFactorDTO(factor) : null;
    }
    
    private SCL90QuestionDTO convertToQuestionDTO(SCL90Question question) {
        return new SCL90QuestionDTO(
                question.getId(),
                question.getQuestionText(),
                question.getFactor()
        );
    }
    
    private SCL90FactorDTO convertToFactorDTO(SCL90Factor factor) {
        return new SCL90FactorDTO(
                factor.getId(),
                factor.getFactorName(),
                factor.getDescription()
        );
    }
} 