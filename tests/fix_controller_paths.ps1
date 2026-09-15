$controllers = @(
    "AcknowledgementController.java",
    "AdminController.java",
    "AIConfigurationController.java",
    "AIController.java",
    "AnalyticsController.java",
    "ClarificationController.java",
    "DepartmentController.java",
    "DocumentController.java",
    "EventController.java",
    "ExecutionController.java",
    "ImportController.java",
    "IntegrationController.java",
    "NotificationController.java",
    "RegulationController.java",
    "RoiMetricController.java",
    "RouteStepController.java",
    "SourceTaskController.java",
    "StorageController.java",
    "TaskAdaptationController.java",
    "TaskRouteController.java",
    "TaskVersionController.java"
)

$basePath = "C:\Users\UserPC\IdeaProjects\AIGen\src\main\java\ru\indutranslator\web\controller"

foreach ($controller in $controllers) {
    $filePath = Join-Path $basePath $controller
    if (Test-Path $filePath) {
        (Get-Content $filePath) -replace '/api/v1/', '/api/' | Set-Content $filePath
        Write-Host "Updated: $controller"
    } else {
        Write-Host "File not found: $controller"
    }
}

Write-Host "Done! Updated all controller paths from /api/v1/ to /api/"
