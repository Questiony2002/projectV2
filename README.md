# 基于端侧语言模型的大学生心理健康助手APP

![项目状态](https://img.shields.io/badge/状态-已完成-brightgreen.svg)
![技术栈](https://img.shields.io/badge/技术栈-Android%20%7C%20Spring%20Boot%20%7C%20LLaMA-blue.svg)
![开发语言](https://img.shields.io/badge/语言-Java%20%7C%20C%2B%2B%20%7C%20Python-orange.svg)
![数据库](https://img.shields.io/badge/ORM-MyBatis--Plus%203.5.3.1-red.svg)
![架构迁移](https://img.shields.io/badge/迁移-JPA→MyBatis--Plus-yellow.svg)

## 📖 项目简介

本项目是一个基于端侧语言模型的大学生心理健康助手Android应用，旨在为大学生提供便捷、私密的心理健康服务。通过集成LLaMA大语言模型，实现本地化的AI心理咨询，保护用户隐私的同时提供专业的心理健康指导。

### 🎯 项目背景

随着现代社会压力的增加，大学生心理健康问题日益突出。传统的心理咨询服务存在预约困难、费用高昂、隐私担忧等问题。本项目利用先进的端侧AI技术，为大学生提供24/7可用的心理健康助手。

## ✨ 核心功能

### 🔐 1. 用户认证模块
- **用户注册**：支持邮箱/用户名注册，密码安全加密
- **用户登录**：JWT Token认证，保持登录状态
- **密码安全**：BCrypt加密存储，支持密码找回

### 🤖 2. AI心理咨询模块
- **端侧推理**：集成LLaMA模型，本地运行保护隐私
- **智能对话**：专业心理咨询对话，支持多轮会话
- **情感分析**：分析用户情绪状态，提供针对性建议
- **聊天记录**：本地存储对话历史，随时回顾

### 📋 3. 心理测试模块
- **SCL-90量表**：权威心理健康评估工具
- **MBTI性格测试**：16型人格测试，了解性格特质
- **测试报告**：详细的测试结果分析和建议
- **历史记录**：查看历史测试结果，跟踪心理状态变化

### 📰 4. 心理资讯模块
- **实时资讯**：自动爬取最新心理健康相关新闻
- **分类浏览**：按主题分类展示资讯内容
- **收藏功能**：收藏感兴趣的文章
- **分享功能**：分享有价值的心理知识

### 👤 5. 个人中心模块
- **个人信息**：编辑个人资料，上传头像
- **测试历史**：查看所有心理测试记录
- **设置中心**：个性化设置，隐私控制
- **数据统计**：个人心理健康数据可视化

## 🛠️ 技术栈

### 前端 (Android App)
- **开发语言**：Java + Kotlin
- **编译版本**：compileSdk 32, targetSdk 33, minSdk 28
- **UI框架**：Android SDK, Material Design
- **网络请求**：Retrofit2 2.9.0 + OkHttp3 4.9.0
- **图片加载**：Glide 4.15.1
- **JSON解析**：Gson 2.8.9
- **本地存储**：SQLite
- **AI推理**：LLaMA C++ Native库 (CMake 3.22.1)
- **异步处理**：Kotlin Coroutines 1.7.3
- **其他组件**：
  - androidx.fragment:fragment:1.5.5
  - androidx.appcompat:appcompat:1.3.0
  - androidx.constraintlayout:constraintlayout:2.0.4
  - androidx.recyclerview:recyclerview:1.2.1
  - androidx.swiperefreshlayout:swiperefreshlayout:1.1.0

### 后端 (Spring Boot)
- **开发语言**：Java 17
- **框架**：Spring Boot 2.7.0
- **数据库**：MySQL 8.0 (mysql-connector-java)
- **ORM**：MyBatis-Plus 3.5.3.1 (替代了原来的JPA)
- **安全认证**：Spring Security + JWT (jjwt 0.9.1)
- **XML绑定**：JAXB API 2.3.1 + JAXB Runtime 2.3.1 (Java 17兼容性)
- **API架构**：RESTful API
- **爬虫模块**：Python + Requests
- **工具库**：Lombok (简化代码)
- **依赖管理**：Maven
- **核心依赖**：
  - spring-boot-starter-web
  - mybatis-plus-boot-starter
  - spring-boot-starter-security
  - javax.xml.bind:jaxb-api
  - org.glassfish.jaxb:jaxb-runtime

### 其他技术
- **版本控制**：Git
- **构建工具**：Maven (后端) + Gradle (前端)
- **数据爬取**：Python爬虫脚本
- **模型部署**：LLaMA端侧部署 (C++17标准)
- **云服务**：阿里云ECS + RDS MySQL
- **文件存储**：本地文件系统 (uploads目录)

## 📁 项目结构

```
projectV2/
├── app/                          # Android前端应用
│   ├── src/main/
│   │   ├── java/com/example/projectv2/
│   │   │   ├── MainActivity.java           # 主界面Activity
│   │   │   ├── LoginActivity.java          # 登录Activity
│   │   │   ├── RegisterActivity.java       # 注册Activity
│   │   │   ├── SplashActivity.java         # 启动页Activity
│   │   │   ├── LLamaAPI.java              # LLaMA模型接口
│   │   │   ├── ModelManager.java           # 模型管理器
│   │   │   ├── ModelDownloadService.java   # 模型下载服务
│   │   │   ├── fragment/                   # Fragment组件
│   │   │   │   ├── AiChatFragment.java     # AI聊天界面
│   │   │   │   ├── SCL90Fragment.java      # SCL-90测试界面
│   │   │   │   ├── MbtiFragment.java       # MBTI测试界面
│   │   │   │   ├── NewsFragment.java       # 资讯界面
│   │   │   │   ├── ProfileFragment.java    # 个人中心界面
│   │   │   │   └── TestSelectionFragment.java # 测试选择界面
│   │   │   ├── api/                        # API接口
│   │   │   │   ├── ApiClient.java          # Retrofit客户端配置
│   │   │   │   ├── UserApi.java            # 用户相关API
│   │   │   │   └── NewsApi.java            # 新闻相关API
│   │   │   ├── model/                      # 数据模型
│   │   │   │   ├── User.java               # 用户实体
│   │   │   │   ├── News.java               # 新闻实体
│   │   │   │   ├── SCL90Question.java      # SCL-90题目
│   │   │   │   ├── SCL90Result.java        # SCL-90结果
│   │   │   │   ├── MbtiQuestion.java       # MBTI题目
│   │   │   │   ├── MbtiType.java           # MBTI类型
│   │   │   │   └── Message.java            # 聊天消息
│   │   │   ├── adapter/                    # 适配器
│   │   │   │   ├── MessageAdapter.java     # 聊天消息适配器
│   │   │   │   └── NewsAdapter.java        # 新闻列表适配器
│   │   │   └── db/                         # 数据库相关
│   │   │       └── ChatDbHelper.java       # 聊天记录数据库
│   │   ├── cpp/                           # C++本地代码
│   │   │   ├── llama-android.cpp          # LLaMA Android实现
│   │   │   └── CMakeLists.txt             # CMake构建配置
│   │   ├── res/                           # Android资源文件
│   │   └── AndroidManifest.xml            # 应用清单文件
│   ├── build.gradle                       # Android构建配置
│   ├── proguard-rules.pro                 # 代码混淆规则
│   ├── build/                             # 构建输出目录
│   └── .cxx/                              # C++构建缓存
│
├── server/                       # Spring Boot后端
│   ├── src/main/
│   │   ├── java/com/example/bluecat/       # 包名已从mental更改为bluecat
│   │   │   ├── BlueCatServerApplication.java     # 应用启动类 (已从MentalHealthApplication重命名)
│   │   │   ├── controller/                 # 控制器层
│   │   │   │   ├── UserController.java     # 用户管理 (/api/user)
│   │   │   │   ├── SCL90Controller.java    # SCL-90测试 (/api/scl90)
│   │   │   │   ├── MbtiController.java     # MBTI测试 (/api/mbti)
│   │   │   │   ├── NewsController.java     # 资讯管理 (/api/news)
│   │   │   │   └── FileUploadController.java # 文件上传 (/api/upload)
│   │   │   ├── service/                    # 服务层
│   │   │   │   ├── UserService.java        # 用户服务接口
│   │   │   │   ├── SCL90Service.java       # SCL-90服务接口
│   │   │   │   ├── MbtiService.java        # MBTI服务接口
│   │   │   │   ├── NewsService.java        # 新闻服务接口
│   │   │   │   └── impl/                   # 服务实现类
│   │   │   │       ├── UserServiceImpl.java
│   │   │   │       ├── SCL90ServiceImpl.java
│   │   │   │       ├── MbtiServiceImpl.java
│   │   │   │       └── NewsServiceImpl.java
│   │   │   ├── mapper/                     # MyBatis-Plus数据访问层
│   │   │   │   ├── UserMapper.java         # 用户数据映射器
│   │   │   │   ├── NewsMapper.java         # 新闻数据映射器
│   │   │   │   ├── MbtiQuestionMapper.java # MBTI题目映射器
│   │   │   │   ├── MbtiTypeMapper.java     # MBTI类型映射器
│   │   │   │   ├── SCL90ResultMapper.java  # SCL-90结果映射器
│   │   │   │   ├── SCLFactorMapper.java    # SCL因子映射器
│   │   │   │   └── SCLQuestionMapper.java  # SCL题目映射器
│   │   │   ├── entity/                     # 实体类 (带MyBatis-Plus注解)
│   │   │   │   ├── User.java               # 用户实体 (@TableName, @TableId)
│   │   │   │   ├── News.java               # 新闻实体 (@TableName, @TableId)
│   │   │   │   ├── SCL90Result.java        # SCL-90结果实体
│   │   │   │   ├── MbtiQuestion.java       # MBTI题目实体
│   │   │   │   ├── MbtiType.java           # MBTI类型实体
│   │   │   │   ├── SCLFactor.java          # SCL因子实体
│   │   │   │   └── SCLQuestion.java        # SCL题目实体
│   │   │   ├── dto/                        # 数据传输对象
│   │   │   │   ├── UserDTO.java            # 用户DTO
│   │   │   │   ├── SCL90QuestionDTO.java   # SCL-90题目DTO
│   │   │   │   ├── SCL90ResultDTO.java     # SCL-90结果DTO
│   │   │   │   ├── MbtiQuestionDTO.java    # MBTI题目DTO
│   │   │   │   └── MbtiTypeDTO.java        # MBTI类型DTO
│   │   │   ├── config/                     # 配置类
│   │   │   │   ├── SecurityConfig.java     # 安全配置
│   │   │   │   ├── FileUploadConfig.java   # 文件上传配置
│   │   │   │   └── PasswordEncoderConfig.java # 密码编码配置
│   │   │   └── security/                   # 安全相关
│   │   │       └── JwtTokenUtil.java       # JWT工具类
│   │   ├── python/
│   │   │   └── news_crawler.py            # 新闻爬虫脚本
│   │   └── resources/
│   │       ├── application.yml            # 应用配置文件 (MyBatis-Plus配置)
│   │       └── mapper/                    # MyBatis XML映射文件
│   │           ├── UserMapper.xml          # 用户映射文件
│   │           ├── NewsMapper.xml          # 新闻映射文件
│   │           ├── MbtiQuestionMapper.xml  # MBTI题目映射文件
│   │           ├── MbtiTypeMapper.xml      # MBTI类型映射文件
│   │           ├── SCL90ResultMapper.xml   # SCL-90结果映射文件
│   │           ├── SCLFactorMapper.xml     # SCL因子映射文件
│   │           └── SCLQuestionMapper.xml   # SCL题目映射文件
│   ├── uploads/                           # 用户上传文件目录
│   │   └── avatars/                       # 用户头像存储
│   └── pom.xml                            # Maven依赖配置 (含MyBatis-Plus和JAXB依赖)
│
└── README.md                     # 项目说明文档
```

## 🔄 架构迁移说明

### MyBatis-Plus 迁移
项目已从Spring Data JPA迁移至MyBatis-Plus，带来以下优势：
- **更灵活的SQL控制**：支持复杂查询和原生SQL
- **高性能**：减少N+1查询问题，优化数据库访问
- **强大的代码生成**：自动生成基础CRUD操作
- **丰富的查询包装器**：链式查询，类型安全
- **分页插件**：内置分页支持

### 技术栈升级
```xml
<!-- MyBatis-Plus核心依赖 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>

<!-- JAXB支持 (Java 17兼容性) -->
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.1</version>
</dependency>
```

## 🚀 快速开始

### 🌐 在线服务

**后端服务部署说明**
- **服务器地址**: `http://YOUR_SERVER_IP:8080`
- **API基础路径**: `http://YOUR_SERVER_IP:8080/api`
- **数据库**: MySQL 8.0

### ⚙️ 部署前配置

**重要提醒**: 在部署前，请确保修改以下配置文件中的IP地址和数据库信息：

1. **Android端API地址**：
   - 文件: `app/src/main/java/com/example/projectv2/api/ApiClient.java`
   - 修改: `BASE_URL` 为您的服务器地址

2. **服务端数据库配置**：
   - 文件: `server/src/main/resources/application.yml`
   - 修改: 数据库连接URL、用户名、密码

### 环境要求

- **JDK**: 17+ (服务器部署)
- **Android Studio**: 4.0+ (本地开发)
- **MySQL**: 8.0+ (已部署至阿里云)
- **Python**: 3.7+ (用于爬虫模块)
- **CMake**: 3.22.1+ (用于C++编译)

### 🏗️ 本地开发环境配置

#### Android应用配置

1. **修改API基础地址**：
   在Android项目中配置您的服务器地址：
   ```java
   // 在 ApiClient.java 中
   public static final String BASE_URL = "http://YOUR_SERVER_IP:8080/";
   ```

2. **网络权限配置**：
   `AndroidManifest.xml` 中已包含必要的网络权限：
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
   <uses-permission android:name="android.permission.usesCleartextTraffic" />
   ```

#### 数据库配置 (仅供参考)

数据库配置示例 (`application.yml`)：
```yaml
spring:
  datasource:
    url: jdbc:mysql://YOUR_DB_HOST:3306/ai_chat_v2?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

# MyBatis-Plus配置
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.bluecat.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 🚀 快速部署

#### 1. Android应用构建

1. 使用Android Studio打开项目根目录
2. **重要**: 修改API地址配置
   - 编辑 `app/src/main/java/com/example/projectv2/api/ApiClient.java`
   - 将 `BASE_URL` 修改为您的服务器地址
   - 示例: `http://YOUR_SERVER_IP:8080/`
3. 等待Gradle同步完成
4. 连接Android设备或启动模拟器
5. 点击运行按钮构建并安装应用

#### 2. 服务器端 (已部署至阿里云)

**✅ 后端服务状态**: 已部署运行
- **部署平台**: 云服务器ECS
- **服务端口**: 8080
- **数据库**: MySQL 8.0
- **访问地址**: http://YOUR_SERVER_IP:8080

**API接口测试**:
```bash
# 测试用户注册
curl -X POST http://YOUR_SERVER_IP:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","email":"test@example.com"}'

# 测试用户登录
curl -X POST http://YOUR_SERVER_IP:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'

# 测试新闻接口
curl -X GET http://YOUR_SERVER_IP:8080/api/news
```

#### 3. 本地后端开发 (可选)

如果需要本地开发调试，可以克隆后端代码：

1. 进入后端目录：
```bash
cd server
```

2. 修改配置文件指向本地数据库：
```bash
# 编辑 src/main/resources/application.yml
# 将数据库地址改为 localhost
```

3. 编译并运行：
```bash
mvn clean install
mvn spring-boot:run
```

**注意**：项目已迁移至MyBatis-Plus，请确保：
- 数据库表结构与实体类匹配
- Mapper XML文件路径正确配置
- MyBatis-Plus依赖已正确导入

### 📊 数据库表结构

项目使用以下7个数据库表：

| 表名 | 功能 | 主要字段 |
|------|------|----------|
| `users` | 用户信息 | id, username, password, email, phone, avatar_url, mbti_type, age, bio, gender, grade, created_at, updated_at |
| `news` | 新闻资讯 | id, title, url, publish_date, created_at |
| `mbti_questions` | MBTI测试题目 | id, question_text, option_a, option_b, dimension |
| `mbti_types` | MBTI人格类型 | type_code, type_name, description, characteristics, strengths, weaknesses |
| `scl90_results` | SCL-90测试结果 | id, user_id, factor_scores, positive_average, positive_items, total_average, total_score |
| `scl_factors` | SCL-90因子定义 | id, factor_name, description |
| `scl_questions` | SCL-90测试题目 | id, question_text, factor, factor_id |

### 🔧 LLaMA模型配置

1. 下载LLaMA模型文件（建议使用GGUF格式）
2. 将模型文件放置在Android设备的指定目录：
   ```
   /storage/emulated/0/Android/data/com.example.projectv2/files/models/
   ```
3. 在应用中配置模型路径

### 📡 API接口详情

#### 用户认证相关
- **POST** `/api/user/register` - 用户注册
- **POST** `/api/user/login` - 用户登录
- **GET** `/api/user/{userId}` - 获取用户信息
- **PUT** `/api/user/{userId}/field` - 更新用户字段
- **PUT** `/api/user/{userId}/password` - 修改密码

#### 心理测试相关
- **GET** `/api/scl90/questions` - 获取SCL-90题目
- **POST** `/api/scl90/results` - 提交SCL-90测试结果
- **GET** `/api/scl90/results/{userId}` - 获取用户测试结果
- **GET** `/api/mbti/questions` - 获取MBTI题目
- **GET** `/api/mbti/types/{typeCode}` - 获取MBTI类型详情
- **PUT** `/api/mbti/user/{userId}` - 更新用户MBTI类型

#### 新闻资讯相关
- **GET** `/api/news` - 获取最新资讯
- **POST** `/api/news/refresh` - 刷新资讯

#### 文件上传相关
- **POST** `/api/upload/avatar` - 上传用户头像

### 🔐 安全配置说明

#### 防火墙设置
确保阿里云服务器安全组已开放以下端口：
- **8080**: Spring Boot应用端口
- **3306**: MySQL数据库端口 (如需外部访问)

#### API访问示例
```javascript
// 前端请求示例
const API_BASE_URL = 'http://YOUR_SERVER_IP:8080/api';

// 用户登录
fetch(`${API_BASE_URL}/user/login`, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        username: 'your_username',
        password: 'your_password'
    })
});
```

## 📱 应用截图

### 主要界面
- 登录注册界面
- AI聊天界面
- 心理测试界面
- 资讯浏览界面
- 个人中心界面

## 🔧 核心特性

### 🏠 端侧推理
- **隐私保护**：模型在本地运行，用户数据不上传
- **离线可用**：无需网络连接即可使用AI功能
- **响应迅速**：本地推理，减少网络延迟

### 🛡️ 安全性
- **数据加密**：用户密码BCrypt加密存储
- **JWT认证**：安全的用户身份验证机制
- **权限控制**：细粒度的功能权限管理

### 📊 专业性
- **权威量表**：使用SCL-90等专业心理评估工具
- **科学算法**：基于心理学理论的评分算法
- **个性化建议**：根据测试结果提供针对性建议

## 🔮 未来规划

### 功能扩展
- [ ] 支持更多心理测试量表
- [ ] 增加语音交互功能
- [ ] 集成更多AI模型选择
- [ ] 添加社区功能模块
- [ ] 支持数据云端同步
- [ ] 增加心理健康数据分析

### 技术优化
- [ ] 利用MyBatis-Plus代码生成器自动生成CRUD代码
- [ ] 实现MyBatis-Plus分页插件优化大数据量查询
- [ ] 添加MyBatis-Plus多租户插件支持
- [ ] 集成MyBatis-Plus性能分析插件
- [ ] 优化复杂查询的SQL性能

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 开源协议

本项目采用 MIT 协议 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 开发团队

- **项目开发人员**:  Questiony, 
- **指导教师**: will Wei

## 📞 联系方式

- **邮箱**: 2640289029@qq.com
- **GitHub**: https://github.com/Questiony2002

---

*本项目为毕业设计作品，旨在探索AI技术在心理健康领域的应用，为大学生群体提供便捷的心理健康服务。* 