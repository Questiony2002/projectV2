@echo off
echo Starting llama-server...
echo.

:: 设置路径和参数
set EXE_PATH=..\build_cpu\bin\Release\llama-server.exe
set MODEL_PATH=..\models\Minicpm-4B-Q4_K_M.gguf
set PORT=8081

:: 检查文件是否存在
if not exist "%EXE_PATH%" (
    echo Error: llama-server.exe not found at %EXE_PATH%
    echo Please make sure you have built the server and copied it to llama/bin directory
    pause
    exit /b
)

if not exist "%MODEL_PATH%" (
    echo Error: Model file not found at %MODEL_PATH%
    echo Please make sure you have downloaded the model and placed it in llama/models directory
    pause
    exit /b
)

:: 执行命令
"%EXE_PATH%" -m "%MODEL_PATH%" --port %PORT% -c 2048 --chat-template minicpm

echo.
echo Server started on port %PORT%.
pause