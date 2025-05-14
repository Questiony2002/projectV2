# AI聊天应用

这是一个基于Android平台的AI聊天应用，提供智能对话功能。

## 项目特点

- 智能对话：集成AI模型，提供自然流畅的对话体验
- 用户友好：直观的界面设计，简单易用
- 安全可靠：采用安全的数据传输和存储机制

## 技术栈

- Android (Java/Kotlin)
- Spring Boot后端服务
- MySQL数据库
- Gradle构建工具

## 项目结构

```
projectV2/
├── app/                    # Android应用主目录
│   ├── src/               # 源代码
│   └── build.gradle       # 应用级构建配置
├── server/                # 后端服务
├── gradle/                # Gradle包装器
├── ai_chat_v2.sql        # 数据库结构
└── build.gradle          # 项目级构建配置
```

## 开始使用

### 环境要求

- Android Studio
- JDK 11或更高版本
- MySQL数据库

### 安装步骤

1. 克隆仓库：
```bash
git clone [仓库URL]
```

2. 导入项目到Android Studio

3. 配置数据库：
   - 导入 `ai_chat_v2.sql` 文件到MySQL
   - 配置数据库连接信息

4. 运行后端服务：
   - Windows: 执行 `start-server.bat`
   - Linux/Mac: 执行 `./gradlew bootRun`

5. 运行Android应用：
   - 通过Android Studio运行到模拟器或实机

## 贡献指南

欢迎提交问题和改进建议！请遵循以下步骤：

1. Fork 项目
2. 创建特性分支
3. 提交改动
4. 推送到分支
5. 创建Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

如有任何问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件至 [您的邮箱地址] 