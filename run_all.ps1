$services = @(
    "discovery-service",
    "gateway-service",
    "identity-service",
    "equipement-service"
)

# Base directory where the script is located
$baseDir = $PSScriptRoot
$backendDir = Join-Path -Path $baseDir -ChildPath "backend"

# Load .env file variables
$envFile = Join-Path -Path $baseDir -ChildPath ".env"
if (Test-Path $envFile) {
    Write-Host "Loading .env file..." -ForegroundColor Cyan
    foreach ($line in Get-Content $envFile) {
        if (![string]::IsNullOrWhiteSpace($line) -and !$line.StartsWith("#")) {
            $parts = $line.Split("=", 2)
            if ($parts.Length -eq 2) {
                Set-Item "env:\$($parts[0].Trim())" "$($parts[1].Trim())"
            }
        }
    }
}

foreach ($service in $services) {
    $servicePath = Join-Path -Path $backendDir -ChildPath $service

    if (Test-Path $servicePath) {
        Write-Host "Starting $service..." -ForegroundColor Green

        # Start the application in a new window
        if ($service -eq "equipement-service") {
            Start-Process -FilePath "powershell" -ArgumentList "-ExecutionPolicy Bypass", "-NoExit", "-Command", "cd '$servicePath'; ..\mvnw.cmd spring-boot:run -DskipTests | Tee-Object -FilePath 'equipement.log'" -WindowStyle Normal
        } else {
            Start-Process -FilePath "powershell" -ArgumentList "-ExecutionPolicy Bypass", "-NoExit", "-Command", "cd '$servicePath'; ..\mvnw.cmd spring-boot:run -DskipTests" -WindowStyle Normal
        }

        if ($service -eq "discovery-service") {
            Write-Host "Waiting 15 seconds for discovery-service to initialize..." -ForegroundColor Cyan
            Start-Sleep -Seconds 15
        }
    } else {
        Write-Host "Directory $servicePath not found. Skipping $service." -ForegroundColor Yellow
    }
}

# Start Frontend
$frontendPath = Join-Path -Path $baseDir -ChildPath "frontend"
if (Test-Path $frontendPath) {
    Write-Host "Starting frontend..." -ForegroundColor Green
    Start-Process -FilePath "powershell" -ArgumentList "-ExecutionPolicy Bypass", "-NoExit", "-Command", "cd '$frontendPath'; npm start" -WindowStyle Normal
} else {
    Write-Host "Directory $frontendPath not found. Skipping frontend." -ForegroundColor Yellow
}

# Start Prediction Service
$predictionPath = Join-Path -Path $backendDir -ChildPath "prediction-service\ai study\work\api"
if (Test-Path $predictionPath) {
    Write-Host "Starting prediction-service..." -ForegroundColor Green
    Start-Process -FilePath "powershell" -ArgumentList "-ExecutionPolicy Bypass", "-NoExit", "-Command", "cd '$predictionPath'; pip install -r requirements.txt; uvicorn main:app --port 8000" -WindowStyle Normal
} else {
    Write-Host "Directory $predictionPath not found. Skipping prediction-service." -ForegroundColor Yellow
}

# Start Node-RED server
Write-Host "Starting Node-RED server..." -ForegroundColor Green
Start-Process -FilePath "powershell" -ArgumentList "-ExecutionPolicy Bypass", "-NoExit", "-Command", "node-red" -WindowStyle Normal

Write-Host "All services started." -ForegroundColor Green
