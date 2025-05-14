@echo off
chcp 65001
echo ===== 启动心理健康应用后端服务 =====
echo.

REM 设置Maven本地仓库路径
set MAVEN_OPTS=-Dmaven.repo.local=D:\Applications\Maven\repository

cd /d "%~dp0server"

echo 当前目录: %CD%
echo.

echo 正在启动Spring Boot应用...
call mvn spring-boot:run -Dmaven.repo.local=D:\Applications\Maven\repository

echo.
echo 如果上面显示错误，请确保：
echo 1. MySQL已启动，并创建了数据库ai_chat_v2
echo 2. 已正确设置JAVA_HOME和Maven环境变量
echo 3. Maven本地仓库路径可访问
echo.

pause 