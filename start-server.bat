@echo off
chcp 65001
echo ===== 正在启动心理健康应用后端服务 =====
echo.

REM 设置Java和Maven环境变量
set JAVA_HOME=D:\Applications\Java
set MAVEN_HOME=D:\Applications\Maven\apache-maven-3.6.1
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

REM 设置Maven本地仓库路径
set MAVEN_OPTS=-Dmaven.repo.local=D:\Applications\Maven\repository

REM 切换到server目录
cd /d "%~dp0server"

echo 当前目录: %CD%
echo.

REM 检查MySQL服务是否启动
echo 检查MySQL服务...
sc query MySQL80 > nul
if %ERRORLEVEL% NEQ 0 (
    echo 警告: MySQL服务可能未启动!
    echo 请确保MySQL服务已启动，数据库ai_chat_v2已创建
    echo.
)

echo 是否需要清理并重新编译项目? (Y/N)
set /p compile=
if /i "%compile%"=="Y" (
    echo 正在清理并编译项目...
    call mvn clean package -DskipTests -Dmaven.repo.local=D:\Applications\Maven\repository
    if %ERRORLEVEL% NEQ 0 (
        echo 编译失败! 按任意键退出...
        pause > nul
        exit /b 1
    )
) else (
    echo 跳过编译步骤...
)

echo.
echo 正在启动Spring Boot应用...
REM 运行Spring Boot应用
call mvn spring-boot:run -Dmaven.repo.local=D:\Applications\Maven\repository

if %ERRORLEVEL% NEQ 0 (
    echo 启动失败! 按任意键退出...
    pause > nul
) else (
    echo 服务已成功启动!
)

pause 