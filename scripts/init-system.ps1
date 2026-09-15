# Initialize Default Enterprise and Source System Script
# This script authenticates and calls the initialize endpoint

$baseUrl = "http://localhost:8080"

# Login request body
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Write-Host "Step 1: Authenticating as admin..." -ForegroundColor Cyan

try {
    # Login
    $loginResponse = Invoke-RestMethod `
        -Uri "$baseUrl/api/auth/login" `
        -Method POST `
        -Body $loginBody `
        -ContentType "application/json"
    
    Write-Host "Login successful!" -ForegroundColor Green
    Write-Host "Token: $($loginResponse.token)" -ForegroundColor Yellow
    
    $token = $loginResponse.token
    
    # Initialize defaults
    Write-Host "`nStep 2: Initializing default enterprise and source system..." -ForegroundColor Cyan
    
    $initResponse = Invoke-RestMethod `
        -Uri "$baseUrl/api/admin/initialize" `
        -Method POST `
        -Headers @{"Authorization" = "Bearer $token"} `
        -ContentType "application/json"
    
    Write-Host "Initialization successful!" -ForegroundColor Green
    Write-Host "Response: $($initResponse | ConvertTo-Json -Depth 5)" -ForegroundColor Yellow
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    if ($_.ErrorDetails) {
        Write-Host "Details: $($_ | ConvertTo-Json -Depth 5)" -ForegroundColor Red
    }
}

Write-Host "`nDone!" -ForegroundColor Cyan
