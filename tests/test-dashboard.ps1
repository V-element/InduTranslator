# Test script to login and test dashboard endpoint
$loginResponse = Invoke-WebRequest -Uri "http://localhost/api/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}'
$token = ($loginResponse.Content | ConvertFrom-Json).token
Write-Host "Token received: $($token.Substring(0, 50))..."

$dashboardResponse = Invoke-WebRequest -Uri "http://localhost/api/analytics/dashboard" -Headers @{"Authorization"="Bearer $token"}
Write-Host "Dashboard response status: $($dashboardResponse.StatusCode)"
Write-Host "Dashboard response content: $($dashboardResponse.Content)"
