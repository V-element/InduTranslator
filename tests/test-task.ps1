# Verify Initialization and Test Task Creation
# This script tests the initialized records and task creation

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
    
    # Test creating a new task - this should use the default enterprise and sourceSystem
    Write-Host "`nStep 2: Creating a test task..." -ForegroundColor Cyan
    
    $taskBody = @{
        title = "Test Task"
        description = "This is a test task to verify initialization"
        status = "DRAFT"
        priority = 1
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod `
            -Uri "$baseUrl/api/tasks" `
            -Method POST `
            -Headers @{"Authorization" = "Bearer $token"} `
            -Body $taskBody `
            -ContentType "application/json"
        
        Write-Host "Task created successfully!" -ForegroundColor Green
        Write-Host ($response | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
        
    } catch {
        Write-Host "Error creating task: $_" -ForegroundColor Red
        Write-Host "Error details: $($_.ErrorDetails)" -ForegroundColor Red
    }
    
    # Test getting all tasks
    Write-Host "`nStep 3: Fetching all tasks..." -ForegroundColor Cyan
    
    try {
        $tasks = Invoke-RestMethod `
            -Uri "$baseUrl/api/tasks" `
            -Method GET `
            -Headers @{"Authorization" = "Bearer $token"} `
            -ContentType "application/json"
        
        Write-Host "Tasks found:" -ForegroundColor Green
        Write-Host ($tasks | ConvertTo-Json -Depth 5) -ForegroundColor Yellow
        
    } catch {
        Write-Host "Error fetching tasks: $_" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}

Write-Host "`nDone!" -ForegroundColor Cyan
