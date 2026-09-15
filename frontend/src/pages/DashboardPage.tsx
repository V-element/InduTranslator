import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { FileText, Clock, CheckCircle2, AlertTriangle, Users, FileText as FileTextIcon, Settings, BarChart3, Upload, Globe, Shield } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@/components/ui/button'

const DashboardPage: React.FC = () => {
  const navigate = useNavigate()
  const {
    data: analytics,
    isLoading,
    error
  } = useQuery({
    queryKey: ['dashboard'],
    queryFn: async () => {
      console.log('Fetching dashboard data...')
      const response = await fetch('/api/analytics/dashboard')
      console.log('Dashboard response:', response.status, response.statusText)
      if (!response.ok) {
        const errorText = await response.text()
        console.error('Dashboard error response:', errorText)
        throw new Error('Ошибка загрузки данных дашборда')
      }
      const data = await response.json()
      console.log('Dashboard data:', data)
      return data
    }
  })

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка данных...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки данных: {(error as Error).message}</AlertDescription>
      </Alert>
    )
  }

  const stats = analytics?.stats || {
    totalTasks: 0,
    completedTasks: 0,
    pendingTasks: 0,
    overdueTasks: 0,
    slaComplianceRate: 0,
    averageCompletionTime: 0
  }

  // Fallback to direct analytics properties if available
  const taskStats = {
    totalTasks: (analytics as any)?.totalTasks || stats.totalTasks || 0,
    completedTasks: (analytics as any)?.completedTasks || stats.completedTasks || 0,
    pendingTasks: (analytics as any)?.pendingTasks || stats.pendingTasks || 0,
    overdueTasks: (analytics as any)?.overdueTasks || stats.overdueTasks || 0,
    slaComplianceRate: (analytics as any)?.slaComplianceRate || stats.slaComplianceRate || 0,
    averageCompletionTime: (analytics as any)?.averageCompletionTime || stats.averageCompletionTime || 0
  }

  const departmentStats = (analytics as any)?.departmentStats || []

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Операционный центр</h1>
        <p className="text-muted-foreground">
          Сводная картина по задачам и узким местам
        </p>
      </div>

      {/* KPI Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Всего задач</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{taskStats.totalTasks}</div>
            <p className="text-xs text-muted-foreground mt-1">
              +{taskStats.pendingTasks} в ожидании
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Выполнено</CardTitle>
            <CheckCircle2 className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{taskStats.completedTasks}</div>
            <p className="text-xs text-muted-foreground mt-1">
              {taskStats.slaComplianceRate}% соблюдения SLA
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">В работе</CardTitle>
            <Clock className="h-4 w-4 text-blue-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{taskStats.pendingTasks}</div>
            <p className="text-xs text-muted-foreground mt-1">
              Среднее время: {taskStats.averageCompletionTime} ч
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Просрочено</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-500">{taskStats.overdueTasks}</div>
            <p className="text-xs text-muted-foreground mt-1">
              Требуют внимания
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Department Distribution */}
      <Card>
        <CardHeader>
          <CardTitle>Распределение по отделам</CardTitle>
        </CardHeader>
        <CardContent>
          {departmentStats && departmentStats.length > 0 ? (
            <div className="space-y-3">
              {departmentStats.map((dept: any) => {
                const percentage = dept.taskCount && dept.completedCount
                  ? Math.round((dept.completedCount / dept.taskCount) * 100)
                  : 0;
                return (
                  <div key={dept.departmentId} className="flex items-center space-x-4">
                    <div className="w-32 text-sm font-medium">{dept.departmentName}</div>
                    <div className="flex-1">
                      <div className="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                        <div 
                          className="h-full bg-blue-500 rounded-full"
                          style={{ width: `${percentage}%` }}
                        />
                      </div>
                    </div>
                    <div className="w-16 text-right text-sm text-muted-foreground">
                      {dept.taskCount || 0} задач
                    </div>
                  </div>
                );
              })}
            </div>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              Нет данных по отделам
            </div>
          )}
        </CardContent>
      </Card>

      {/* Navigation Section */}
      <Card>
        <CardHeader>
          <CardTitle>Навигация</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/tasks')}
            >
              <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01" />
              </svg>
              <span className="font-medium">Задачи</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/departments')}
            >
              <Users className="h-6 w-6" />
              <span className="font-medium">Отделы</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/documents')}
            >
              <FileTextIcon className="h-6 w-6" />
              <span className="font-medium">Документы</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/adaptations')}
            >
              <Settings className="h-6 w-6" />
              <span className="font-medium">Адаптации задач</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/regulations')}
            >
              <Shield className="h-6 w-6" />
              <span className="font-medium">Регламенты</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/analytics')}
            >
              <BarChart3 className="h-6 w-6" />
              <span className="font-medium">Аналитика</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/import')}
            >
              <Upload className="h-6 w-6" />
              <span className="font-medium">Импорт</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/integrations')}
            >
              <Globe className="h-6 w-6" />
              <span className="font-medium">Интеграции</span>
            </Button>
            
            <Button 
              variant="outline" 
              className="h-20 flex flex-col items-center justify-center gap-2"
              onClick={() => navigate('/admin')}
            >
              <Shield className="h-6 w-6" />
              <span className="font-medium">Админка</span>
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Recent Activity */}
      <Card>
        <CardHeader>
          <CardTitle>Активные инциденты</CardTitle>
        </CardHeader>
        <CardContent>
          {analytics?.recentEvents && analytics.recentEvents.length > 0 ? (
            <div className="space-y-4">
              {analytics.recentEvents.map((event: any) => (
                <div key={event.id} className="flex items-start space-x-4 p-3 rounded-lg bg-gray-50">
                  <div className="h-10 w-10 rounded-full bg-red-100 flex items-center justify-center flex-shrink-0">
                    <AlertTriangle className="h-5 w-5 text-red-600" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="font-medium truncate">{event.title}</div>
                    <div className="text-sm text-muted-foreground mt-1">
                      {event.description}
                    </div>
                    <div className="text-xs text-muted-foreground mt-2">
                      {event.timestamp}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              Нет активных инцидентов
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default DashboardPage
