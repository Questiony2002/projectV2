package com.example.bluecat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bluecat.entity.SCL90Factor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SCL90FactorMapper extends BaseMapper<SCL90Factor> {
    
    // 获取所有因子
    List<SCL90Factor> findAllFactors();
    
    // 根据因子名称获取因子
    SCL90Factor findByFactorName(String factorName);
    
    // 根据ID获取因子
    SCL90Factor findById(Integer id);
} 