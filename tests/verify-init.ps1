# Verify Default Enterprise and Source System Records
# This script verifies the records were created correctly

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
    
    $token = $loginResponse.token
    
    # Get all enterprises
    Write-Host "`nStep 2: Fetching all enterprises..." -ForegroundColor Cyan
    
    $enterprises = Invoke-RestMethod `
        -Uri "$baseUrl/api/enterprises" `
        -Method GET `
        -Headers @{"Authorization" = "Bearer $token"} `
        -ContentType "application/json"
    
    Write-Host "Enterprises found:" -ForegroundColor Green
    Write-Host ($enterprises | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
    
    # Get all source systems
    Write-Host "`nStep 3: Fetching all source systems..." -ForegroundColor Cyan
    
    $sourceSystems = Invoke-RestMethod `
        -Uri "$baseUrl/api/source-systems" `
        -Method GET `
        -Headers @{"Authorization" = "Bearer $token"} `
        -ContentType "application/json"
    
    Write-Host "Source Systems found:" -ForegroundColor Green
    Write-Host ($sourceSystems | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
    
    # Test SourceTaskController.findById(1L) equivalent
    Write-Host "`nStep 4: Testing task controller with default IDs..." -ForegroundColor Cyan
    
    try {
        $task = Invoke-RestMethod `
            -Uri "$baseUrl/api/source-tasks/1" `
            -Method GET `
            -Headers @{"Authorization" = "Bearer $token"} `
            -ContentType "application/json"
        
        Write-Host "Task 1 fetched successfully:" -ForegroundColor Green
        Write-Host ($task | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
    } catch {
        Write-Host "Task 1 not found (expected if no tasks created yet): $_" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}

Write-Host "`nDone!" -ForegroundColor Cyan
