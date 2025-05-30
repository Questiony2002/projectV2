package com.example.bluecat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bluecat.entity.SCL90Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SCL90QuestionMapper extends BaseMapper<SCL90Question> {
    
    // 获取所有问题
    List<SCL90Question> findAllQuestions();
    
    // 根据因子获取问题
    List<SCL90Question> findQuestionsByFactor(String factor);
} 