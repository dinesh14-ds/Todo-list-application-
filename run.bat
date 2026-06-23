@echo off
echo Compiling Java project...
javac -d bin src\*.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)
echo Running Ds task...
java -cp bin src.TodoApp
