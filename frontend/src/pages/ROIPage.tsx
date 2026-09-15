import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { FileText, TrendingUp, DollarSign, Percent } from 'lucide-react'
import { AnalyticsAPI } from '@/services/analyticsService'

const ROIPage: React.FC = () => {
  const {
    data: analytics,
    isLoading,
    error
  } = useQuery({
    queryKey: ['roi'],
    queryFn: () => AnalyticsAPI.getROIAnalytics()
  })

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка ROI данных...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки ROI: {(error as Error).message}</AlertDescription>
      </Alert>
    )
  }

  const roiData = analytics?.roi || []
  const totalSavings = analytics?.totalSavings || 0
  const totalInvestment = analytics?.totalInvestment || 0
  const roiPercentage = analytics?.roiPercentage || 0

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">ROI / Экономический эффект</h1>
        <p className="text-muted-foreground">
          Анализ экономической эффективности
        </p>
      </div>

      {/* Key Metrics */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Общий эффект</CardTitle>
            <DollarSign className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {totalSavings.toLocaleString()} ₽
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Совокупная экономия
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Инвестиции</CardTitle>
            <TrendingUp className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {totalInvestment.toLocaleString()} ₽
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Вложено в автоматизацию
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">ROI</CardTitle>
            <Percent className="h-4 w-4 text-purple-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-purple-600">
              {roiPercentage.toFixed(1)}%
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Возврат инвестиций
            </p>
          </CardContent>
        </Card>
      </div>

      {/* ROI Metrics Table */}
      <Card>
        <CardHeader>
          <CardTitle>Метрики ROI по отделам</CardTitle>
        </CardHeader>
        <CardContent>
          {roiData.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет данных по ROI</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b">
                    <th className="text-left py-2 px-4 text-sm font-medium">Отдел</th>
                    <th className="text-right py-2 px-4 text-sm font-medium">Метрика</th>
                    <th className="text-right py-2 px-4 text-sm font-medium">Базовый</th>
                    <th className="text-right py-2 px-4 text-sm font-medium">Текущий</th>
                    <th className="text-right py-2 px-4 text-sm font-medium">Эффект</th>
                    <th className="text-right py-2 px-4 text-sm font-medium">ROI %</th>
                  </tr>
                </thead>
                <tbody>
                  {roiData.map((metric: any) => (
                    <tr key={metric.id} className="border-b hover:bg-gray-50">
                      <td className="py-2 px-4 text-sm">{metric.departmentName}</td>
                      <td className="py-2 px-4 text-sm text-right">{metric.metricName}</td>
                      <td className="py-2 px-4 text-sm text-right">{metric.baselineValue.toLocaleString()}</td>
                      <td className="py-2 px-4 text-sm text-right">{metric.currentValue.toLocaleString()}</td>
                      <td className="py-2 px-4 text-sm text-right text-green-600">
                        {metric.improvementValue.toLocaleString()}
                      </td>
                      <td className="py-2 px-4 text-sm text-right text-purple-600">
                        {metric.improvementPercentage.toFixed(1)}%
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Savings Breakdown */}
      <Card>
        <CardHeader>
          <CardTitle>Разбивка экономии по направлениям</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {analytics?.savingsByCategory?.map((item: any) => (
              <div key={item.category}>
                <div className="flex justify-between mb-1">
                  <span className="text-sm font-medium">{item.category}</span>
                  <span className="text-sm font-medium">{item.value.toLocaleString()} ₽ ({item.percentage}%)</span>
                </div>
                <div className="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                  <div 
                    className="h-full bg-green-500 rounded-full"
                    style={{ width: `${item.percentage}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default ROIPage
