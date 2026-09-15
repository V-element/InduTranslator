import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { FileText, Users, Plus, Edit, Trash2, ChevronLeft } from 'lucide-react'
import { DepartmentAPI } from '@/services/departmentService'

const DepartmentsPage: React.FC = () => {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newDepartment, setNewDepartment] = useState({ name: '', description: '', code: '' })

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['departments', page, size, statusFilter],
    queryFn: () => {
      return DepartmentAPI.getAllDepartments(
        page,
        size,
        undefined,
        statusFilter === 'all' ? undefined : statusFilter === 'active'
      )
    }
  })

  const departments = response?.content || []
  const totalPages = response?.totalPages || 0

  const handleAddDepartment = async () => {
    try {
      if (!newDepartment.name.trim()) {
        alert('Пожалуйста, введите название отдела')
        return
      }
      await DepartmentAPI.createDepartment({
        name: newDepartment.name,
        description: newDepartment.description || '',
        code: newDepartment.code || ''
      })
      setIsAddDialogOpen(false)
      setNewDepartment({ name: '', description: '', code: '' })
      alert('Отдел успешно создан!')
    } catch (err) {
      console.error('Failed to add department:', err)
      alert('Ошибка при создании отдела')
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка отделов...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки отделов: {(error as Error).message}</AlertDescription>
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
        <h1 className="text-3xl font-bold tracking-tight">Управление отделами</h1>
        <p className="text-muted-foreground">
          Организационная структура и компетенции
        </p>
      </div>

      {/* Actions */}
      <div className="flex justify-between items-center">
        <div className="flex gap-2">
          <Button variant="outline" size="sm">
            <FileText className="mr-2 h-4 w-4" />
            Иерархия
          </Button>
          <Button variant="outline" size="sm">
            <Users className="mr-2 h-4 w-4" />
            Состав
          </Button>
        </div>
        <Button onClick={() => setIsAddDialogOpen(true)}>
          <Plus className="mr-2 h-4 w-4" />
          Новый отдел
        </Button>
        <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
          <DialogContent className="sm:max-w-md">
            <DialogHeader>
              <DialogTitle>Новый отдел</DialogTitle>
            </DialogHeader>
            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label htmlFor="name">Название</Label>
                <Input
                  id="name"
                  value={newDepartment.name}
                  onChange={(e) => setNewDepartment({ ...newDepartment, name: e.target.value })}
                  placeholder="Введите название отдела"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="code">Код</Label>
                <Input
                  id="code"
                  value={newDepartment.code}
                  onChange={(e) => setNewDepartment({ ...newDepartment, code: e.target.value })}
                  placeholder="Введите код отдела"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="description">Описание</Label>
                <Input
                  id="description"
                  value={newDepartment.description}
                  onChange={(e) => setNewDepartment({ ...newDepartment, description: e.target.value })}
                  placeholder="Введите описание"
                />
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setIsAddDialogOpen(false)}>
                Отмена
              </Button>
              <Button onClick={handleAddDepartment}>
                Сохранить
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Departments Tree */}
      <Card>
        <CardHeader>
          <CardTitle>
            Организационная структура ({departments.length} отделов)
          </CardTitle>
        </CardHeader>
        <CardContent>
          {departments.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет отделов</p>
            </div>
          ) : (
            <div className="space-y-2">
              {departments.map((dept) => (
                <div key={dept.id} className="flex items-center justify-between p-3 border rounded-lg hover:bg-gray-50">
                  <div className="flex items-center gap-3">
                    <div className="flex items-center justify-center w-8 h-8 rounded-full bg-blue-100 text-blue-600">
                      <Users className="h-4 w-4" />
                    </div>
                    <div>
                      <div className="font-medium">{dept.name}</div>
                      {dept.description && (
                        <div className="text-xs text-muted-foreground">
                          {dept.description}
                        </div>
                      )}
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className={`text-xs px-2 py-1 rounded-full ${
                      dept.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {dept.isActive ? 'Активен' : 'Неактивен'}
                    </span>
                    <Button variant="ghost" size="sm">
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button variant="ghost" size="sm">
                      <Trash2 className="h-4 w-4" />
                    </Button>
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

export default DepartmentsPage
