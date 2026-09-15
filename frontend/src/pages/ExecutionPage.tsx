import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Clock, CheckCircle2, AlertTriangle, ChevronDown, ChevronUp, FileText } from 'lucide-react'
import { ExecutionAPI } from '@/services/executionService'

const ExecutionPage: React.FC = () => {
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [expandedSteps, setExpandedSteps] = useState<number[]>([])

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['executions', page, size, statusFilter],
    queryFn: () => {
      return ExecutionAPI.getAllExecutions(
        page,
        size,
        undefined,
        statusFilter === 'all' ? undefined : statusFilter
      )
    }
  })

  const executions = response?.content || []
  const totalPages = response?.totalPages || 0

  const toggleStep = (id: number) => {
    setExpandedSteps(prev => 
      prev.includes(id) ? prev.filter(sid => sid !== id) : [...prev, id]
    )
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'completed': return 'bg-green-100 text-green-800'
      case 'in-progress': return 'bg-blue-100 text-blue-800'
      case 'pending': return 'bg-gray-100 text-gray-800'
      case 'blocked': return 'bg-red-100 text-red-800'
      case 'cancelled': return 'bg-orange-100 text-orange-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка исполнений...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки исполнений: {(error as Error).message}</AlertDescription>
      </Alert>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Исполнение</h1>
        <p className="text-muted-foreground">
          Мониторинг этапов выполнения задач
        </p>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-5">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Всего этапов</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{executions.length}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Выполнено</CardTitle>
            <CheckCircle2 className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {executions.filter(e => e.status === 'completed').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">В работе</CardTitle>
            <Clock className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {executions.filter(e => e.status === 'in-progress').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">В ожидании</CardTitle>
            <AlertTriangle className="h-4 w-4 text-yellow-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-yellow-600">
              {executions.filter(e => e.status === 'pending').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Заблокировано</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {executions.filter(e => e.status === 'blocked').length}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filter */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="w-full md:w-48">
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
              >
                <option value="all">Все статусы</option>
                <option value="pending">В ожидании</option>
                <option value="in-progress">В работе</option>
                <option value="completed">Выполнено</option>
                <option value="blocked">Заблокировано</option>
                <option value="cancelled">Отменено</option>
              </select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Executions List */}
      <Card>
        <CardHeader>
          <CardTitle>
            Список этапов ({executions.length} шт.)
          </CardTitle>
        </CardHeader>
        <CardContent>
          {executions.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет этапов выполнения</p>
            </div>
          ) : (
            <div className="space-y-4">
              {executions.map((execution) => (
                <div key={execution.id} className="border rounded-lg p-4 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <h3 className="font-medium text-lg">{execution.stepName || 'Без названия'}</h3>
                        <Badge className={getStatusColor(execution.status)}>
                          {execution.status === 'completed' ? 'Выполнено' :
                           execution.status === 'in-progress' ? 'В работе' :
                           execution.status === 'pending' ? 'Ожидание' :
                           execution.status === 'blocked' ? 'Заблокировано' : 'Отменено'}
                        </Badge>
                      </div>

                      <div className="text-sm text-muted-foreground mt-2">
                        {execution.taskTitle && <span className="mr-4">Задача: {execution.taskTitle}</span>}
                        {execution.departmentName && <span className="mr-4">Отдел: {execution.departmentName}</span>}
                        {execution.assignedToFullName && <span className="mr-4">Ответственный: {execution.assignedToFullName}</span>}
                      </div>

                      {execution.slaHours && (
                        <div className="mt-3">
                          <div className="text-xs text-muted-foreground mb-1">SLA прогресс</div>
                          <div className="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                            <div 
                              className={`h-full ${
                                execution.slaStatus === 'expired' ? 'bg-red-500' :
                                execution.slaStatus === 'at-risk' ? 'bg-yellow-500' : 'bg-green-500'
                              }`}
                              style={{ width: `${Math.min(100, (execution.slaHours / 24) * 100)}%` }}
                            />
                          </div>
                          <div className="text-xs text-muted-foreground mt-1">
                            {execution.slaStatus === 'expired' ? 'Просрочено' :
                             execution.slaStatus === 'at-risk' ? 'Под угрозой' : 'В срок'}
                          </div>
                        </div>
                      )}

                      {expandedSteps.includes(execution.id) && (
                        <div className="mt-4 pt-3 border-t space-y-2">
                          {execution.comments && execution.comments.length > 0 && (
                            <div>
                              <span className="text-xs text-muted-foreground block mb-1">Комментарии</span>
                              {execution.comments.map((comment, idx) => (
                                <div key={idx} className="text-sm text-muted-foreground pl-2 border-l-2 border-gray-200">
                                  {comment}
                                </div>
                              ))}
                            </div>
                          )}
                          {execution.acknowledgements && execution.acknowledgements.length > 0 && (
                            <div>
                              <span className="text-xs text-muted-foreground block mb-1">Подтверждения</span>
                              <div className="flex flex-wrap gap-2">
                                {execution.acknowledgements.map((ack, idx) => (
                                  <Badge key={idx} variant="secondary">
                                    {ack.username}
                                  </Badge>
                                ))}
                              </div>
                            </div>
                          )}
                          {execution.attachments && execution.attachments.length > 0 && (
                            <div>
                              <span className="text-xs text-muted-foreground block mb-1">Вложения</span>
                              <div className="flex flex-wrap gap-2">
                                {execution.attachments.map((attachment, idx) => (
                                  <Badge key={idx} variant="outline">
                                    {attachment.fileName}
                                  </Badge>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                      )}

                      <Button 
                        variant="ghost" 
                        size="sm" 
                        className="mt-2"
                        onClick={() => toggleStep(execution.id)}
                      >
                        {expandedSteps.includes(execution.id) ? (
                          <>
                            Скрыть детали <ChevronUp className="ml-1 h-4 w-4" />
                          </>
                        ) : (
                          <>
                            Показать детали <ChevronDown className="ml-1 h-4 w-4" />
                          </>
                        )}
                      </Button>
                    </div>
                    <div className="flex gap-2 ml-4">
                      {execution.status === 'pending' && (
                        <Button variant="outline" size="sm">
                          Начать
                        </Button>
                      )}
                      {execution.status === 'in-progress' && (
                        <Button variant="outline" size="sm">
                          Завершить
                        </Button>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex items-center justify-between mt-4">
          <div className="text-sm text-muted-foreground">
            Страница {page + 1} из {totalPages}
          </div>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPage(Math.max(0, page - 1))}
              disabled={page === 0}
            >
              Назад
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPage(Math.min(totalPages - 1, page + 1))}
              disabled={page === totalPages - 1}
            >
              Вперед
            </Button>
          </div>
        </div>
      )}
    </div>
  )
}

export default ExecutionPage
