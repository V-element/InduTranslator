import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { FileText, TrendingUp, Clock, AlertTriangle } from 'lucide-react'
import { AnalyticsAPI } from '@/services/analyticsService'

const AnalyticsPage: React.FC = () => {
  const [period, setPeriod] = useState('month')

  const {
    data: analytics,
    isLoading,
    error
  } = useQuery({
    queryKey: ['analytics', period],
    queryFn: () => AnalyticsAPI.getDashboardAnalytics()
  })

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка аналитики...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки аналитики: {(error as Error).message}</AlertDescription>
      </Alert>
    )
  }

  const periodLabels: Record<string, string> = {
    week: 'Неделя',
    month: 'Месяц',
    quarter: 'Квартал',
    year: 'Год'
  }

  const getStatusText = (status: string): string => {
    const statusMap: Record<string, string> = {
      'pending': 'В ожидании',
      'in-progress': 'В работе',
      'completed': 'Выполнено',
      'overdue': 'Просрочено',
      'draft': 'Черновик'
    }
    return statusMap[status] || status
  }

  if (!analytics || !analytics.stats) {
    return (
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Аналитика</h1>
          <p className="text-muted-foreground">
            Детальная аналитика и отчеты
          </p>
        </div>
        <div className="text-center py-12">
          <FileText className="mx-auto h-12 w-12 mb-4 text-muted-foreground" />
          <h3 className="text-lg font-medium">Нет данных для аналитики</h3>
          <p className="text-sm text-muted-foreground mt-2">
            Аналитика будет доступна после создания задач
          </p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Аналитика</h1>
        <p className="text-muted-foreground">
          Детальная аналитика и отчеты
        </p>
      </div>

      {/* Period Filter */}
      <div className="flex gap-2 mb-6">
        {Object.entries(periodLabels).map(([key, label]) => (
          <button
            key={key}
            onClick={() => setPeriod(key)}
            className={`px-4 py-2 rounded-md text-sm font-medium ${
              period === key
                ? 'bg-blue-600 text-white'
                : 'bg-white border text-gray-700 hover:bg-gray-50'
            }`}
          >
            {label}
          </button>
        ))}
      </div>

      {/* KPI Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Всего задач</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{analytics?.stats?.totalTasks || 0}</div>
            <p className="text-xs text-muted-foreground mt-1">
              {analytics?.stats?.totalTasksChange || 0} vs предыдущий период
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Соблюдение SLA</CardTitle>
            <TrendingUp className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {analytics?.stats?.slaComplianceRate || 0}%
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {analytics?.stats?.slaChange || 0} п.п. vs предыдущий период
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Среднее время</CardTitle>
            <Clock className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {analytics?.stats?.averageCompletionTime || 0} ч
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {analytics?.stats?.timeChange || 0} мин vs предыдущий период
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Просрочено</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {analytics?.stats?.overdueTasks || 0}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {analytics?.stats?.overdueChange || 0} vs предыдущий период
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Analytics Tabs */}
      <div className="grid gap-6 md:grid-cols-2">
        {/* Department Distribution */}
        <Card>
          <CardHeader>
            <CardTitle>Распределение по отделам</CardTitle>
          </CardHeader>
          <CardContent>
            {analytics?.departmentBreakdown && analytics.departmentBreakdown.length > 0 ? (
              <div className="space-y-3">
                {analytics.departmentBreakdown.map((dept: any) => (
                  <div key={dept.departmentName} className="flex items-center gap-4">
                    <div className="w-32 text-sm font-medium">{dept.departmentName}</div>
                    <div className="flex-1">
                      <div className="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                        <div 
                          className="h-full bg-blue-500 rounded-full"
                          style={{ width: `${dept.percentage}%` }}
                        />
                      </div>
                    </div>
                    <div className="w-16 text-right text-sm">
                      {dept.count} задач
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-muted-foreground">
                Нет данных
              </div>
            )}
          </CardContent>
        </Card>

        {/* Status Distribution */}
        <Card>
          <CardHeader>
            <CardTitle>Распределение по статусам</CardTitle>
          </CardHeader>
          <CardContent>
            {analytics?.statusBreakdown && analytics.statusBreakdown.length > 0 ? (
              <div className="space-y-2">
                {analytics.statusBreakdown.map((status: any) => (
                  <div key={status.status} className="flex items-center gap-3">
                    <div className={`w-3 h-3 rounded-full ${
                      status.status === 'completed' ? 'bg-green-500' :
                      status.status === 'pending' ? 'bg-blue-500' :
                      status.status === 'overdue' ? 'bg-red-500' : 'bg-gray-500'
                    }`} />
                    <div className="flex-1">
                      <div className="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                        <div 
                          className="h-full bg-gray-600 rounded-full"
                          style={{ width: `${status.percentage}%` }}
                        />
                      </div>
                    </div>
                    <div className="w-24 text-right text-sm font-medium">
                      {getStatusText(status.status)}
                    </div>
                    <div className="w-12 text-right text-sm text-muted-foreground">
                      {status.count}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-muted-foreground">
                Нет данных
              </div>
            )}
          </CardContent>
        </Card>
      </div>

      {/* Timeline */}
      <Card>
        <CardHeader>
          <CardTitle>Динамика задач за период</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="h-64 flex items-center justify-center text-muted-foreground">
            [График динамики будет здесь - ECharts]
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default AnalyticsPage
