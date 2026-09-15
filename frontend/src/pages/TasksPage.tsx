import React, { useState } from 'react'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter, DialogTrigger } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { 
  Table, 
  TableBody, 
  TableCell, 
  TableHead, 
  TableHeader, 
  TableRow 
} from '@/components/ui/table'
import { 
  Select, 
  SelectContent, 
  SelectItem, 
  SelectTrigger, 
  SelectValue 
} from '@/components/ui/select'
import { FileText, Search, Filter, Download, Upload, Plus, X, ChevronLeft } from 'lucide-react'
import { TaskAPI } from '@/services/taskService'
import { Badge } from '@/components/ui/badge'

const TasksPage: React.FC = () => {
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [sort, setSort] = useState('createdAt')
  const [filter, setFilter] = useState('')
  const [statusFilter, setStatusFilter] = useState('all')
  const [departmentFilter, setDepartmentFilter] = useState('all')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [isImportDialogOpen, setIsImportDialogOpen] = useState(false)
  const [newTask, setNewTask] = useState({ title: '', description: '', status: 'pending', priority: 1 })
  const [isFilterOpen, setIsFilterOpen] = useState(false)

  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['tasks', page, size, sort, filter, statusFilter, departmentFilter],
    queryFn: async () => {
      try {
        if (filter) {
          console.log('Searching tasks with filter:', filter)
          return await TaskAPI.searchTasks(filter, page, size)
        } else {
          console.log('Fetching all tasks with filters:', { page, size, sort, statusFilter, departmentFilter })
          return await TaskAPI.getAllTasks(page, size, sort, filter, statusFilter, departmentFilter)
        }
      } catch (err) {
        console.error('Error fetching tasks:', err)
        throw err
      }
    }
  })

  // Debug: Log response structure
  React.useEffect(() => {
    if (response) {
      const tasksData = response as any
      console.log('Tasks response:', { response, tasksCount: tasksData.content?.length, totalPages: tasksData.totalPages })
    }
  }, [response])

  const tasks = response?.content || []
  const totalPages = response?.totalPages || 0
  const totalElements = response?.totalElements || 0

  // Status translations
  const getStatusText = (status: string): string => {
    const statusMap: Record<string, string> = {
      'pending': 'В ожидании',
      'in-progress': 'В работе',
      'completed': 'Выполнено',
      'overdue': 'Просрочено',
      'draft': 'Черновик',
      'DRAFT': 'Черновик',
      'PENDING': 'В ожидании',
      'IN_PROGRESS': 'В работе',
      'COMPLETED': 'Выполнено',
      'OVERDUE': 'Просрочено'
    }
    return statusMap[status] || status
  }

  const handleAddTask = async () => {
    try {
      if (!newTask.title.trim()) {
        alert('Пожалуйста, введите название задачи')
        return
      }
      await TaskAPI.createTask({
        title: newTask.title,
        description: newTask.description || '',
        status: newTask.status,
        priority: newTask.priority || 1
      })
      setIsAddDialogOpen(false)
      setNewTask({ title: '', description: '', status: 'pending', priority: 1 })
      // Invalidate and refetch tasks query
      await queryClient.invalidateQueries({ queryKey: ['tasks'] })
    } catch (err) {
      console.error('Failed to add task:', err)
      alert('Ошибка при добавлении задачи')
    }
  }

  const handleImport = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (file) {
      try {
        const count = await TaskAPI.importTasks(file)
        setIsImportDialogOpen(false)
        // Invalidate and refetch tasks query
        await queryClient.invalidateQueries({ queryKey: ['tasks'] })
      } catch (err) {
        console.error('Failed to import tasks:', err)
        alert('Ошибка при импорте задач')
      }
    }
  }

  const handleExport = async () => {
    try {
      const blob = await TaskAPI.exportTasks()
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = 'tasks.csv'
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
      window.URL.revokeObjectURL(url)
    } catch (err) {
      console.error('Failed to export tasks:', err)
      alert('Ошибка при экспорте задач')
    }
  }

  return (
    <div className="space-y-6">
      {/* Back Button */}
      <div>
        <Button variant="outline" size="sm" onClick={() => navigate(-1)}>
          <ChevronLeft className="mr-2 h-4 w-4" />
          Назад
        </Button>
      </div>
      
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Задачи</h1>
        <p className="text-muted-foreground">
          Поиск и управление задачами
        </p>
      </div>

      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Button 
            variant="outline" 
            size="sm" 
            onClick={() => setIsFilterOpen(!isFilterOpen)}
          >
            <Filter className="mr-2 h-4 w-4" />
            Фильтры
          </Button>
          <Button variant="outline" size="sm" onClick={handleExport}>
            <Download className="mr-2 h-4 w-4" />
            Экспорт
          </Button>
        </div>
        <div className="flex items-center gap-2">
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="default" size="sm">
                <Plus className="mr-2 h-4 w-4" />
                Добавить
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-md">
              <DialogHeader>
                <DialogTitle>Добавить задачу</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 py-4">
                <div className="space-y-2">
                  <Label htmlFor="title">Название</Label>
                  <Input
                    id="title"
                    value={newTask.title}
                    onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
                    placeholder="Введите название задачи"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="description">Описание</Label>
                  <Input
                    id="description"
                    value={newTask.description}
                    onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
                    placeholder="Введите описание"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="status">Статус</Label>
                  <Select
                    value={newTask.status}
                    onValueChange={(value) => setNewTask({ ...newTask, status: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Выберите статус" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="pending">В ожидании</SelectItem>
                      <SelectItem value="PENDING">В ожидании</SelectItem>
                      <SelectItem value="in-progress">В работе</SelectItem>
                      <SelectItem value="IN_PROGRESS">В работе</SelectItem>
                      <SelectItem value="completed">Выполнено</SelectItem>
                      <SelectItem value="COMPLETED">Выполнено</SelectItem>
                      <SelectItem value="overdue">Просрочено</SelectItem>
                      <SelectItem value="OVERDUE">Просрочено</SelectItem>
                      <SelectItem value="draft">Черновик</SelectItem>
                      <SelectItem value="DRAFT">Черновик</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="priority">Приоритет (1-10)</Label>
                  <Input
                    id="priority"
                    type="number"
                    min="1"
                    max="10"
                    value={newTask.priority}
                    onChange={(e) => setNewTask({ ...newTask, priority: parseInt(e.target.value) || 1 })}
                  />
                </div>
              </div>
              <DialogFooter>
                <Button variant="outline" onClick={() => setIsAddDialogOpen(false)}>
                  Отмена
                </Button>
                <Button onClick={handleAddTask}>
                  Сохранить
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <Dialog open={isImportDialogOpen} onOpenChange={setIsImportDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" size="sm">
                <Upload className="mr-2 h-4 w-4" />
                Импорт
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-md">
              <DialogHeader>
                <DialogTitle>Импорт задач из CSV</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 py-4">
                <div className="text-sm text-muted-foreground">
                  Формат CSV: Название,Описание,Статус,Приоритет
                </div>
                <div className="space-y-2">
                  <Label htmlFor="file">Файл CSV</Label>
                  <Input
                    id="file"
                    type="file"
                    accept=".csv"
                    onChange={handleImport}
                  />
                </div>
              </div>
              <DialogFooter>
                <Button variant="outline" onClick={() => setIsImportDialogOpen(false)}>
                  Закрыть
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Filters */}
      {isFilterOpen && (
        <Card>
          <CardContent className="pt-6">
            <div className="flex flex-col md:flex-row gap-4">
              {/* Search */}
              <div className="flex-1">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Поиск задач..."
                    value={filter}
                    onChange={(e) => setFilter(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>

              {/* Status Filter */}
              <div className="w-full md:w-48">
                <Select value={statusFilter} onValueChange={setStatusFilter}>
                  <SelectTrigger>
                    <SelectValue placeholder="Статус" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">Все статусы</SelectItem>
                    <SelectItem value="pending">В ожидании</SelectItem>
                    <SelectItem value="PENDING">В ожидании</SelectItem>
                    <SelectItem value="in-progress">В работе</SelectItem>
                    <SelectItem value="IN_PROGRESS">В работе</SelectItem>
                    <SelectItem value="completed">Выполнено</SelectItem>
                    <SelectItem value="COMPLETED">Выполнено</SelectItem>
                    <SelectItem value="overdue">Просрочено</SelectItem>
                    <SelectItem value="OVERDUE">Просрочено</SelectItem>
                    <SelectItem value="draft">Черновик</SelectItem>
                    <SelectItem value="DRAFT">Черновик</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {/* Department Filter */}
              <div className="w-full md:w-48">
                <Select value={departmentFilter} onValueChange={setDepartmentFilter}>
                  <SelectTrigger>
                    <SelectValue placeholder="Отдел" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">Все отделы</SelectItem>
                    <SelectItem value="it">IT</SelectItem>
                    <SelectItem value="hr">HR</SelectItem>
                    <SelectItem value="finance">Финансы</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Tasks Table */}
      <Card>
        <CardHeader>
          <CardTitle>
            Список задач ({totalElements} шт.)
          </CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="text-center py-8">Загрузка задач...</div>
          ) : error ? (
            <div className="text-red-500 text-center py-8">
              Ошибка загрузки: {(error as Error).message}
            </div>
          ) : tasks.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет задач, соответствующих фильтрам</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID</TableHead>
                    <TableHead>Название</TableHead>
                    <TableHead>Статус</TableHead>
                    <TableHead>Отдел</TableHead>
                    <TableHead>Приоритет</TableHead>
                    <TableHead>Создано</TableHead>
                    <TableHead>Действия</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {tasks.map((task) => (
                    <TableRow key={task.id}>
                      <TableCell className="font-mono text-sm">{task.id}</TableCell>
                      <TableCell className="font-medium">
                        {task.title || 'Без названия'}
                      </TableCell>
                      <TableCell>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          task.status === 'completed' || task.status === 'COMPLETED'
                            ? 'bg-green-100 text-green-800'
                            : task.status === 'overdue' || task.status === 'OVERDUE'
                            ? 'bg-red-100 text-red-800'
                            : task.status === 'draft' || task.status === 'DRAFT'
                            ? 'bg-gray-100 text-gray-800'
                            : 'bg-blue-100 text-blue-800'
                        }`}>
                          {getStatusText(task.status)}
                        </span>
                      </TableCell>
                      <TableCell>{task.departmentName || '-'}</TableCell>
                      <TableCell>
                        <div className="flex items-center">
                          <div className="h-2 w-2 rounded-full mr-2" style={{
                            backgroundColor: 
                              task.priority && task.priority >= 5 ? '#ef4444' :
                              task.priority && task.priority >= 3 ? '#f59e0b' :
                              '#10b981'
                          }} />
                          <span className="text-sm">{task.priority || 0}</span>
                        </div>
                      </TableCell>
                      <TableCell className="text-sm text-muted-foreground">
                        {new Date(task.createdAt || '').toLocaleDateString()}
                      </TableCell>
                      <TableCell>
                        <Button variant="ghost" size="sm" onClick={() => navigate(`/tasks/${task.id}`)}>Открыть</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>

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
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default TasksPage
