import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { FileText, Clock, CheckCircle2, AlertTriangle, History, Diff, ChevronLeft, Plus } from 'lucide-react'
import { RegulationAPI } from '@/services/regulationService'

const RegulationsPage: React.FC = () => {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newRegulation, setNewRegulation] = useState({ title: '', code: '', description: '', departmentId: '', routeId: '' })

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['regulations', page, size, statusFilter],
    queryFn: () => {
      return RegulationAPI.getAllRegulations(
        page,
        size,
        undefined,
        undefined,
        statusFilter === 'all' ? undefined : statusFilter === 'active'
      )
    }
  })

  const regulations = response?.content || []
  const totalPages = response?.totalPages || 0

  const handleAddRegulation = async () => {
    try {
      if (!newRegulation.title.trim()) {
        alert('Пожалуйста, введите название регламента')
        return
      }
      await RegulationAPI.createRegulation({
        title: newRegulation.title,
        code: newRegulation.code || '',
        description: newRegulation.description || '',
        departmentId: newRegulation.departmentId ? parseInt(newRegulation.departmentId) : undefined,
        routeId: newRegulation.routeId ? parseInt(newRegulation.routeId) : undefined
      })
      setIsAddDialogOpen(false)
      setNewRegulation({ title: '', code: '', description: '', departmentId: '', routeId: '' })
      alert('Регламент успешно создан!')
    } catch (err) {
      console.error('Failed to add regulation:', err)
      alert('Ошибка при создании регламента')
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка регламентов...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки регламентов: {(error as Error).message}</AlertDescription>
      </Alert>
    )
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
        <h1 className="text-3xl font-bold tracking-tight">Регламенты</h1>
        <p className="text-muted-foreground">
          Управление регламентами и их версиями
        </p>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Всего регламентов</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{regulations.length}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Активные</CardTitle>
            <CheckCircle2 className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {regulations.filter(r => r.isActive).length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Неактивные</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {regulations.filter(r => !r.isActive).length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Версий</CardTitle>
            <History className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {/* Will show total versions when we implement version counting */}
              {regulations.length}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Add Regulation Button */}
      <div className="flex justify-end">
        <Button onClick={() => setIsAddDialogOpen(true)}>
          <Plus className="mr-2 h-4 w-4" />
          Добавить регламент
        </Button>
      </div>

      <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Новый регламент</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="title">Название</Label>
              <Input
                id="title"
                value={newRegulation.title}
                onChange={(e) => setNewRegulation({ ...newRegulation, title: e.target.value })}
                placeholder="Введите название регламента"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="code">Код</Label>
              <Input
                id="code"
                value={newRegulation.code}
                onChange={(e) => setNewRegulation({ ...newRegulation, code: e.target.value })}
                placeholder="Введите код регламента"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="description">Описание</Label>
              <Input
                id="description"
                value={newRegulation.description}
                onChange={(e) => setNewRegulation({ ...newRegulation, description: e.target.value })}
                placeholder="Введите описание"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="departmentId">Отдел</Label>
              <Input
                id="departmentId"
                type="number"
                value={newRegulation.departmentId}
                onChange={(e) => setNewRegulation({ ...newRegulation, departmentId: e.target.value })}
                placeholder="ID отдела (опционально)"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="routeId">Маршрут</Label>
              <Input
                id="routeId"
                type="number"
                value={newRegulation.routeId}
                onChange={(e) => setNewRegulation({ ...newRegulation, routeId: e.target.value })}
                placeholder="ID маршрута (опционально)"
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsAddDialogOpen(false)}>
              Отмена
            </Button>
            <Button onClick={handleAddRegulation}>
              Сохранить
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

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
                <option value="active">Активные</option>
                <option value="inactive">Неактивные</option>
              </select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Regulations List */}
      <Card>
        <CardHeader>
          <CardTitle>
            Список регламентов ({regulations.length} шт.)
          </CardTitle>
        </CardHeader>
        <CardContent>
          {regulations.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет регламентов</p>
            </div>
          ) : (
            <div className="space-y-4">
              {regulations.map((regulation) => (
                <div key={regulation.id} className="border rounded-lg p-4 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start">
                    <div>
                      <div className="flex items-center gap-2 mb-2">
                        <h3 className="font-medium text-lg">{regulation.title || 'Без названия'}</h3>
                        <Badge className={regulation.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}>
                          {regulation.isActive ? 'Активен' : 'Неактивен'}
                        </Badge>
                        <Badge variant="outline">
                          Версия {regulation.version}
                        </Badge>
                      </div>
                      
                      <div className="text-sm text-muted-foreground mt-2">
                        {regulation.description}
                      </div>

                      <div className="flex items-center gap-4 mt-3 text-xs text-muted-foreground">
                        <span className="flex items-center gap-1">
                          <Clock className="h-3 w-3" />
                          Код: {regulation.code}
                        </span>
                        {regulation.departmentName && (
                          <span className="flex items-center gap-1">
                            <FileText className="h-3 w-3" />
                            Отдел: {regulation.departmentName}
                          </span>
                        )}
                        {regulation.routeName && (
                          <span className="flex items-center gap-1">
                            <History className="h-3 w-3" />
                            Маршрут: {regulation.routeName}
                          </span>
                        )}
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <Button variant="outline" size="sm">
                        <Diff className="h-4 w-4 mr-2" />
                        Сравнить
                      </Button>
                      <Button variant="outline" size="sm">
                        <History className="h-4 w-4 mr-2" />
                        Версии
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

export default RegulationsPage
