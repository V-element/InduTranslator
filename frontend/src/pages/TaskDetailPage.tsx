import React from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Badge } from '@/components/ui/badge'
import { FileText, Users, Clock, Calendar, ChevronLeft } from 'lucide-react'
import { TaskAPI } from '@/services/taskService'

const TaskDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const taskId = id ? parseInt(id, 10) : 0

  const {
    data: task,
    isLoading,
    error
  } = useQuery({
    queryKey: ['task', taskId],
    queryFn: () => TaskAPI.getTaskById(taskId),
    enabled: !!taskId
  })

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка задачи...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error || !task) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки задачи: {(error as Error).message}</AlertDescription>
      </Alert>
    )
  }

  const statusColors: Record<string, string> = {
    completed: 'bg-green-100 text-green-800',
    pending: 'bg-blue-100 text-blue-800',
    'in-progress': 'bg-yellow-100 text-yellow-800',
    overdue: 'bg-red-100 text-red-800',
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

  return (
    <div className="space-y-6">
      {/* Back button */}
      <Button variant="outline" size="sm" onClick={() => navigate(-1)}>
        <ChevronLeft className="mr-2 h-4 w-4" />
        Назад
      </Button>

      {/* Task Header */}
      <Card>
        <CardHeader>
          <div className="flex justify-between items-start">
            <div>
              <CardTitle className="text-2xl">{task.title || 'Без названия'}</CardTitle>
              <div className="flex items-center gap-2 mt-2">
                <Badge className={statusColors[task.status] || 'bg-gray-100 text-gray-800'}>
                  {getStatusText(task.status)}
                </Badge>
                {task.priority && (
                  <Badge variant="outline">
                    Приоритет: {task.priority}
                  </Badge>
                )}
              </div>
            </div>
            <div className="flex gap-2">
              <Button variant="outline" size="sm">Редактировать</Button>
              <Button variant="outline" size="sm">Клонировать</Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {task.description && (
            <div className="prose max-w-none mb-4">
              <h4 className="font-medium text-sm text-muted-foreground">Описание</h4>
              <p className="text-sm">{task.description}</p>
            </div>
          )}
          
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
            {task.departmentName && (
              <div className="flex items-center gap-2">
                <Users className="h-4 w-4 text-muted-foreground" />
                <div>
                  <div className="text-xs text-muted-foreground">Отдел</div>
                  <div className="text-sm">{task.departmentName}</div>
                </div>
              </div>
            )}
            
            {task.createdAt && (
              <div className="flex items-center gap-2">
                <Calendar className="h-4 w-4 text-muted-foreground" />
                <div>
                  <div className="text-xs text-muted-foreground">Создано</div>
                  <div className="text-sm">{new Date(task.createdAt).toLocaleDateString()}</div>
                </div>
              </div>
            )}
            
            {task.updatedAt && (
              <div className="flex items-center gap-2">
                <Clock className="h-4 w-4 text-muted-foreground" />
                <div>
                  <div className="text-xs text-muted-foreground">Обновлено</div>
                  <div className="text-sm">{new Date(task.updatedAt).toLocaleDateString()}</div>
                </div>
              </div>
            )}
            
            <div className="flex items-center gap-2">
              <FileText className="h-4 w-4 text-muted-foreground" />
              <div>
                <div className="text-xs text-muted-foreground">ID</div>
                <div className="text-sm font-mono">{task.id}</div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabs */}
      <Tabs defaultValue="adaptations" className="w-full">
        <TabsList className="grid grid-cols-4 mb-4">
          <TabsTrigger value="adaptations">Адаптации</TabsTrigger>
          <TabsTrigger value="history">История</TabsTrigger>
          <TabsTrigger value="documents">Документы</TabsTrigger>
          <TabsTrigger value="actions">Действия</TabsTrigger>
        </TabsList>

        <TabsContent value="adaptations" className="space-y-4">
          {task.id ? (
            <Card>
              <CardHeader>
                <CardTitle>AI-адаптации</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-center py-8 text-muted-foreground">
                  Список адаптаций будет доступен после интеграции API
                </div>
              </CardContent>
            </Card>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              Загрузите задачу для просмотра адаптаций
            </div>
          )}
        </TabsContent>

        <TabsContent value="history" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>История изменений</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8 text-muted-foreground">
                История изменений будет доступна после интеграции API
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="documents" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Связанные документы</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8 text-muted-foreground">
                Связанные документы будут доступны после интеграции API
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="actions" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Доступные действия</CardTitle>
            </CardHeader>
            <CardContent className="flex gap-2 flex-wrap">
              <Button variant="outline">Назначить</Button>
              <Button variant="outline">Перенаправить</Button>
              <Button variant="outline">Добавить комментарий</Button>
              <Button variant="outline">Прикрепить файл</Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

export default TaskDetailPage
