$uri = "http://localhost/api/auth/login"
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri $uri -Method POST -ContentType "application/json" -Body $body
Write-Output $response
