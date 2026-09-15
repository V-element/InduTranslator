import React, { useState } from 'react'
import { useQuery, useMutation, QueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { FileText, Settings, Plus, RefreshCw, CheckCircle2, AlertTriangle, ChevronLeft } from 'lucide-react'
import { IntegrationAPI } from '@/services/integrationService'

const queryClient = new QueryClient()

const IntegrationsPage: React.FC = () => {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [newIntegration, setNewIntegration] = useState({
    name: '',
    code: '',
    description: '',
    type: 'api' as 'api' | 'database' | 'file',
    apiUrl: '',
    active: true
  })

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['integrations', page, size, statusFilter],
    queryFn: () => {
      return IntegrationAPI.getAllIntegrations(
        page,
        size,
        undefined,
        undefined,
        statusFilter === 'all' ? undefined : statusFilter === 'active'
      )
    }
  })

  const createIntegrationMutation = useMutation({
    mutationFn: IntegrationAPI.createIntegration,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['integrations'] })
      setIsCreateDialogOpen(false)
      setNewIntegration({
        name: '',
        code: '',
        description: '',
        type: 'api',
        apiUrl: '',
        active: true
      })
    }
  })

  const handleCreateIntegration = () => {
    createIntegrationMutation.mutate(newIntegration)
  }

  const integrations = response?.content || []
  const totalPages = response?.totalPages || 0

  const handleTestConnection = async (id: number) => {
    try {
      const result = await IntegrationAPI.testConnection(id)
      console.log('Test result:', result)
    } catch (error) {
      console.error('Test failed:', error)
    }
  }

  const handleSync = async (id: number) => {
    try {
      const result = await IntegrationAPI.syncNow(id)
      console.log('Sync result:', result)
    } catch (error) {
      console.error('Sync failed:', error)
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка интеграций...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки интеграций: {(error as Error).message}</AlertDescription>
      </Alert>
    )
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'success': return 'text-green-600'
      case 'error': return 'text-red-600'
      case 'in-progress': return 'text-blue-600'
      case 'never': return 'text-gray-400'
      default: return 'text-gray-600'
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <Button variant="outline" size="sm" onClick={() => navigate(-1)}>
          <ChevronLeft className="mr-2 h-4 w-4" />
          Назад
        </Button>
      </div>
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Интеграции</h1>
        <p className="text-muted-foreground">
          Подключение внешних систем
        </p>
      </div>

      {/* Actions */}
      <div className="flex justify-between items-center">
        <div className="flex gap-2">
          <Button variant="outline" size="sm">
            <FileText className="mr-2 h-4 w-4" />
            Карта данных
          </Button>
          <Button variant="outline" size="sm">
            <Settings className="mr-2 h-4 w-4" />
            Настройки
          </Button>
        </div>
        <Button onClick={() => setIsCreateDialogOpen(true)}>
          <Plus className="mr-2 h-4 w-4" />
          Новая интеграция
        </Button>
      </div>

      {/* Integrations List */}
      <Card>
        <CardHeader>
          <CardTitle>
            Список интеграций ({integrations.length} шт.)
          </CardTitle>
        </CardHeader>
        <CardContent>
          {integrations.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет интеграций</p>
            </div>
          ) : (
            <div className="space-y-4">
              {integrations.map((integration) => (
                <div key={integration.id} className="border rounded-lg p-4 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <h3 className="font-medium text-lg">{integration.name}</h3>
                        <span className="text-xs text-muted-foreground">[{integration.code}]</span>
                        <span className={`text-xs px-2 py-1 rounded-full ${
                          integration.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {integration.active ? 'Активна' : 'Неактивна'}
                        </span>
                        <span className={`text-xs px-2 py-1 rounded-full ${
                          integration.type === 'api' ? 'bg-blue-100 text-blue-800' :
                          integration.type === 'database' ? 'bg-purple-100 text-purple-800' :
                          integration.type === 'file' ? 'bg-orange-100 text-orange-800' :
                          'bg-gray-100 text-gray-800'
                        }`}>
                          {integration.type}
                        </span>
                      </div>
                      
                      {integration.description && (
                        <div className="text-sm text-muted-foreground mb-3">
                          {integration.description}
                        </div>
                      )}

                      <div className="flex items-center gap-4 text-sm">
                        {integration.apiUrl && (
                          <div className="flex items-center gap-1 text-muted-foreground">
                            <Settings className="h-3 w-3" />
                            {integration.apiUrl}
                          </div>
                        )}
                        <div className={`flex items-center gap-1 ${getStatusColor(integration.lastStatus || 'never')}`}>
                          <RefreshCw className={`h-3 w-3 ${integration.lastStatus === 'in-progress' ? 'animate-spin' : ''}`} />
                          Последний синхронизирован:
                          {integration.lastSyncAt 
                            ? new Date(integration.lastSyncAt).toLocaleDateString() 
                            : 'Никогда'}
                        </div>
                        {integration.lastErrorMessage && (
                          <div className="flex items-center gap-1 text-red-600">
                            <AlertTriangle className="h-3 w-3" />
                            {integration.lastErrorMessage}
                          </div>
                        )}
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <Button variant="outline" size="sm" onClick={() => handleTestConnection(integration.id)}>
                        <CheckCircle2 className="h-4 w-4 mr-2" />
                        Тест
                      </Button>
                      <Button variant="outline" size="sm" onClick={() => handleSync(integration.id)}>
                        <RefreshCw className="h-4 w-4 mr-2" />
                        Синхр.
                      </Button>
                      <Button variant="outline" size="sm">
                        Редактировать
                      </Button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      {/* Create Integration Dialog */}
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Новая интеграция</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="name">Название</Label>
              <Input
                id="name"
                value={newIntegration.name}
                onChange={(e) => setNewIntegration({ ...newIntegration, name: e.target.value })}
                placeholder="Например: API Мой склад"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="code">Код интеграции</Label>
              <Input
                id="code"
                value={newIntegration.code}
                onChange={(e) => setNewIntegration({ ...newIntegration, code: e.target.value })}
                placeholder="mywarehouse_api"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="description">Описание</Label>
              <Textarea
                id="description"
                value={newIntegration.description}
                onChange={(e) => setNewIntegration({ ...newIntegration, description: e.target.value })}
                placeholder="Интеграция с API Мой склад"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="type">Тип интеграции</Label>
              <select
                id="type"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                value={newIntegration.type}
                onChange={(e) => setNewIntegration({ ...newIntegration, type: e.target.value as 'api' | 'database' | 'file' })}
              >
                <option value="api">API</option>
                <option value="database">База данных</option>
                <option value="file">Файл</option>
              </select>
            </div>
            {newIntegration.type === 'api' && (
              <div className="space-y-2">
                <Label htmlFor="apiUrl">API URL</Label>
                <Input
                  id="apiUrl"
                  value={newIntegration.apiUrl}
                  onChange={(e) => setNewIntegration({ ...newIntegration, apiUrl: e.target.value })}
                  placeholder="https://api.example.com/v1"
                />
              </div>
            )}
            <div className="flex items-center space-x-2">
              <input
                type="checkbox"
                id="active"
                checked={newIntegration.active}
                onChange={(e) => setNewIntegration({ ...newIntegration, active: e.target.checked })}
                className="h-4 w-4 rounded border-gray-300 text-primary focus:ring-primary"
              />
              <Label htmlFor="active">Активна</Label>
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
              Отмена
            </Button>
            <Button onClick={handleCreateIntegration} disabled={createIntegrationMutation.isPending}>
              {createIntegrationMutation.isPending ? 'Создание...' : 'Создать'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

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

export default IntegrationsPage
