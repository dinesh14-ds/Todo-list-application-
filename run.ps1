Write-Host "Compiling Java project..." -ForegroundColor Cyan
javac -d bin src/*.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed." -ForegroundColor Red
    return
}
Write-Host "Running Ds task..." -ForegroundColor Green
java -cp bin src.TodoApp
