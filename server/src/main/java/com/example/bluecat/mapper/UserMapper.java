package com.example.bluecat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bluecat.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层
 * 继承BaseMapper<User>，自动获得基础的CRUD操作方法
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    // ==================== 自定义查询方法 ====================
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息，如果不存在返回null
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 用户信息，如果不存在返回null
     */
    User findByEmail(@Param("email") String email);
    
    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @return 用户信息，如果不存在返回null
     */
    User findByPhone(@Param("phone") String phone);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在的用户数量（0或1）
     */
    int countByUsername(@Param("username") String username);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱地址
     * @return 存在的用户数量（0或1）
     */
    int countByEmail(@Param("email") String email);
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 存在的用户数量（0或1）
     */
    int countByPhone(@Param("phone") String phone);
    
    /**
     * 更新用户MBTI类型
     * @param userId 用户ID
     * @param mbtiType MBTI类型代码
     */
    void updateMbtiType(@Param("userId") Long userId, @Param("mbtiType") String mbtiType);
    
    // ==================== BaseMapper提供的方法说明 ====================
    // 以下方法由MyBatis-Plus的BaseMapper自动提供，无需手动实现
    
    /**
     * 根据ID查询用户
     * 使用方法：userMapper.selectById(userId)
     * @param id 用户ID
     * @return 用户信息，如果不存在返回null
     */
    // User selectById(Serializable id);
    
    /**
     * 插入新用户
     * 使用方法：userMapper.insert(user)
     * @param entity 用户实体对象
     * @return 影响的行数
     */
    // int insert(User entity);
    
    /**
     * 根据ID更新用户信息（更新所有字段）
     * 使用方法：userMapper.updateById(user)
     * @param entity 用户实体对象
     * @return 影响的行数
     */
    // int updateById(User entity);
    
    /**
     * 根据ID删除用户
     * 使用方法：userMapper.deleteById(userId)
     * @param id 用户ID
     * @return 影响的行数
     */
    // int deleteById(Serializable id);
    
    /**
     * 查询所有用户列表
     * 使用方法：userMapper.selectList(null) 或 userMapper.selectList(queryWrapper)
     * @param queryWrapper 查询条件包装器，传null查询所有
     * @return 用户列表
     */
    // List<User> selectList(Wrapper<User> queryWrapper);
    
    /**
     * 根据条件查询用户数量
     * 使用方法：userMapper.selectCount(queryWrapper)
     * @param queryWrapper 查询条件包装器
     * @return 用户数量
     */
    // Integer selectCount(Wrapper<User> queryWrapper);
} 