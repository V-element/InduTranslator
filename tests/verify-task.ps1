# Verify Default Enterprise and Source System Records in Database
# This script tests the initialized records through SourceTaskController

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
    
    # Test SourceTaskController.findById(1L) - this should load enterprise and sourceSystem
    Write-Host "`nStep 2: Testing SourceTaskController.findById(1L)..." -ForegroundColor Cyan
    
    try {
        $task = Invoke-RestMethod `
            -Uri "$baseUrl/api/source-tasks/1" `
            -Method GET `
            -Headers @{"Authorization" = "Bearer $token"} `
            -ContentType "application/json"
        
        Write-Host "Task 1 fetched successfully:" -ForegroundColor Green
        Write-Host ($task | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
        
        # Check if enterprise and sourceSystem are loaded
        if ($task.enterprise) {
            Write-Host "`nEnterprise loaded: $($task.enterprise.name) (ID: $($task.enterprise.id))" -ForegroundColor Green
        } else {
            Write-Host "`nEnterprise: null" -ForegroundColor Red
        }
        
        if ($task.sourceSystem) {
            Write-Host "SourceSystem loaded: $($task.sourceSystem.name) (ID: $($task.sourceSystem.id))" -ForegroundColor Green
        } else {
            Write-Host "SourceSystem: null" -ForegroundColor Red
        }
        
    } catch {
        Write-Host "Task 1 not found or error: $_" -ForegroundColor Yellow
    }
    
    # Check if enterprise exists via SourceTask entity directly
    Write-Host "`nStep 3: Checking enterprise through source-tasks endpoint..." -ForegroundColor Cyan
    
    try {
        $tasks = Invoke-RestMethod `
            -Uri "$baseUrl/api/source-tasks" `
            -Method GET `
            -Headers @{"Authorization" = "Bearer $token"} `
            -ContentType "application/json"
        
        Write-Host "Source Tasks found:" -ForegroundColor Green
        Write-Host ($tasks | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
        
    } catch {
        Write-Host "Error fetching tasks: $_" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}

Write-Host "`nDone!" -ForegroundColor Cyan
