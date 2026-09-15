// Debug script to check exact API response structure
$baseUrl = "http://localhost:8080"

$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

try {
    # Login
    $loginResponse = Invoke-RestMethod `
        -Uri "$baseUrl/api/auth/login" `
        -Method POST `
        -Body $loginBody `
        -ContentType "application/json"
    
    $token = $loginResponse.token
    
    # Get all tasks with debug info
    Write-Host "Checking exact API response structure..." -ForegroundColor Cyan
    
    $response = Invoke-RestMethod `
        -Uri "$baseUrl/api/tasks?page=0&size=10" `
        -Method GET `
        -Headers @{"Authorization" = "Bearer $token"} `
        -ContentType "application/json"
    
    Write-Host "Response type: $($response.GetType().FullName)" -ForegroundColor Yellow
    Write-Host "Response properties:" -ForegroundColor Yellow
    $response.PSObject.Properties | ForEach-Object { Write-Host "  $($_.Name) = $($_.Value)" -ForegroundColor White }
    
    Write-Host "`nChecking for 'content' property..." -ForegroundColor Cyan
    if ($response.PSObject.Properties.Name -contains "content") {
        Write-Host "  'content' exists: $(if ($response.content) { 'YES' } else { 'NULL' })" -ForegroundColor Green
        Write-Host "  Content type: $($response.content.GetType().FullName)" -ForegroundColor Yellow
        Write-Host "  Content count: $($response.content.Count)" -ForegroundColor Yellow
    } else {
        Write-Host "  'content' property NOT FOUND!" -ForegroundColor Red
    }
    
    Write-Host "`nChecking for 'totalElements' property..." -ForegroundColor Cyan
    if ($response.PSObject.Properties.Name -contains "totalElements") {
        Write-Host "  'totalElements' exists: $($response.totalElements)" -ForegroundColor Green
    } else {
        Write-Host "  'totalElements' property NOT FOUND!" -ForegroundColor Red
    }
    
    # Check if response is wrapped
    Write-Host "`nChecking if response is wrapped in another object..." -ForegroundColor Cyan
    if ($response -is [array]) {
        Write-Host "  Response is an array!" -ForegroundColor Yellow
    } elseif ($response -is [PSCustomObject]) {
        Write-Host "  Response is a PSCustomObject" -ForegroundColor Yellow
        # Check if it's wrapped
        if ($response.PSObject.Properties.Name -contains "data") {
            Write-Host "  Response has 'data' property - checking content..." -ForegroundColor Yellow
            $dataResponse = $response.data
            if ($dataResponse.PSObject.Properties.Name -contains "content") {
                Write-Host "  Wrapped 'content' exists with $($dataResponse.content.Count) items" -ForegroundColor Green
            }
        }
    }
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
