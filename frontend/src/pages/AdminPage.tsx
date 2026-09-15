import React, { useState } from 'react'
import { useQuery, useMutation, QueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { FileText, Users, Key, Settings, Plus, Edit, Trash2, Activity, ChevronLeft } from 'lucide-react'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter, DialogDescription } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { AdminAPI } from '@/services/adminService'

const queryClient = new QueryClient()

const AdminPage: React.FC = () => {
  const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState('users')
  const [userPage, setUserPage] = useState(0)
  const [userSize, setUserSize] = useState(10)
  const [rolePage, setRolePage] = useState(0)
  const [roleSize, setRoleSize] = useState(10)
  const [isUserDialogOpen, setIsUserDialogOpen] = useState(false)
  const [isRoleDialogOpen, setIsRoleDialogOpen] = useState(false)
  const [newUser, setNewUser] = useState({ username: '', email: '', firstName: '', lastName: '', password: '' })
  const [newRole, setNewRole] = useState({ name: '', code: '', description: '' })

  const {
    data: usersResponse,
    isLoading: usersLoading,
    error: usersError
  } = useQuery({
    queryKey: ['admin-users', userPage, userSize],
    queryFn: () => AdminAPI.getUsers(userPage, userSize)
  })

  const {
    data: rolesResponse,
    isLoading: rolesLoading,
    error: rolesError
  } = useQuery({
    queryKey: ['admin-roles', rolePage, roleSize],
    queryFn: () => AdminAPI.getRoles(rolePage, roleSize)
  })

  const createUserMutation = useMutation({
    mutationFn: AdminAPI.createUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-users'] })
      setIsUserDialogOpen(false)
      setNewUser({ username: '', email: '', firstName: '', lastName: '', password: '' })
    }
  })

  const createRoleMutation = useMutation({
    mutationFn: AdminAPI.createRole,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-roles'] })
      setIsRoleDialogOpen(false)
      setNewRole({ name: '', code: '', description: '' })
    }
  })

  const handleCreateUser = () => {
    createUserMutation.mutate(newUser)
  }

  const handleCreateRole = () => {
    createRoleMutation.mutate(newRole)
  }

  const users = usersResponse && Array.isArray(usersResponse) ? usersResponse : (usersResponse as any)?.content || []
  const roles = rolesResponse && Array.isArray(rolesResponse) ? rolesResponse : (rolesResponse as any)?.content || []
  const totalPages = (usersResponse as any)?.totalPages || 1
  const totalRoles = (rolesResponse as any)?.totalPages || 1

  if (usersLoading || rolesLoading) {
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

  if (usersError || rolesError) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки: {(usersError || rolesError as Error).message}</AlertDescription>
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
        <h1 className="text-3xl font-bold tracking-tight">Администратор</h1>
        <p className="text-muted-foreground">
          Управление пользователями, ролями и конфигурацией
        </p>
      </div>

      {/* Tabs */}
      <div className="flex border-b">
        <button
          onClick={() => setActiveTab('users')}
          className={`px-6 py-3 border-b-2 font-medium text-sm ${
            activeTab === 'users'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          }`}
        >
          <Users className="h-4 w-4 inline mr-2" />
          Пользователи ({users.length})
        </button>
        <button
          onClick={() => setActiveTab('roles')}
          className={`px-6 py-3 border-b-2 font-medium text-sm ${
            activeTab === 'roles'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          }`}
        >
          <Key className="h-4 w-4 inline mr-2" />
          Роли ({roles.length})
        </button>
        <button
          onClick={() => setActiveTab('config')}
          className={`px-6 py-3 border-b-2 font-medium text-sm ${
            activeTab === 'config'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          }`}
        >
          <Settings className="h-4 w-4 inline mr-2" />
          Конфигурация
        </button>
        <button
          onClick={() => setActiveTab('analytics')}
          className={`px-6 py-3 border-b-2 font-medium text-sm ${
            activeTab === 'analytics'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          }`}
        >
          <Activity className="h-4 w-4 inline mr-2" />
          Аналитика
        </button>
      </div>

      {/* Users Tab */}
      {activeTab === 'users' && (
        <Card>
          <CardHeader>
            <div className="flex justify-between items-center">
              <CardTitle>Управление пользователями</CardTitle>
              <Button onClick={() => setIsUserDialogOpen(true)}>
                <Plus className="mr-2 h-4 w-4" />
                Новый пользователь
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            {users.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
                <p>Нет пользователей</p>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left py-2 px-4">Имя</th>
                      <th className="text-left py-2 px-4">Email</th>
                      <th className="text-left py-2 px-4">Отделы</th>
                      <th className="text-left py-2 px-4">Роли</th>
                      <th className="text-left py-2 px-4">Статус</th>
                      <th className="text-left py-2 px-4">Действия</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user: any) => (
                      <tr key={user.id} className="border-b hover:bg-gray-50">
                        <td className="py-2 px-4">
                          {user.firstName} {user.lastName} ({user.username})
                        </td>
                        <td className="py-2 px-4">{user.email}</td>
                        <td className="py-2 px-4">
                          {user.departmentNames?.join(', ') || '-'}
                        </td>
                        <td className="py-2 px-4">
                          {user.roleNames?.join(', ') || '-'}
                        </td>
                        <td className="py-2 px-4">
                          <span className={`text-xs px-2 py-1 rounded-full ${
                            user.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                          }`}>
                            {user.active ? 'Активен' : 'Неактивен'}
                          </span>
                        </td>
                        <td className="py-2 px-4 flex gap-2">
                          <Button variant="ghost" size="sm">
                            <Edit className="h-4 w-4" />
                          </Button>
                          <Button variant="ghost" size="sm">
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardContent>
        </Card>
      )}

      {/* Roles Tab */}
      {activeTab === 'roles' && (
        <Card>
          <CardHeader>
            <div className="flex justify-between items-center">
              <CardTitle>Управление ролями</CardTitle>
              <Button onClick={() => setIsRoleDialogOpen(true)}>
                <Plus className="mr-2 h-4 w-4" />
                Новая роль
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            {roles.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
                <p>Нет ролей</p>
              </div>
            ) : (
              <div className="space-y-2">
                {roles.map((role: any) => (
                  <div key={role.id} className="flex items-center justify-between p-3 border rounded-lg hover:bg-gray-50">
                    <div>
                      <div className="font-medium">{role.name}</div>
                      <div className="text-xs text-muted-foreground">
                        {role.description}
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <span className={`text-xs px-2 py-1 rounded-full ${
                        role.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      }`}>
                        {role.isActive ? 'Активна' : 'Неактивна'}
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
      )}

      {/* Config Tab */}
      {activeTab === 'config' && (
        <Card>
          <CardHeader>
            <CardTitle>Конфигурация системы</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div>
                <h3 className="font-medium mb-2">AI провайдеры</h3>
                <p className="text-sm text-muted-foreground">
                  Настройка подключений к AI моделям
                </p>
              </div>
              <div>
                <h3 className="font-medium mb-2">ROI коэффициенты</h3>
                <p className="text-sm text-muted-foreground">
                  Настройка коэффициентов для расчета ROI
                </p>
              </div>
              <div>
                <h3 className="font-medium mb-2">Системные настройки</h3>
                <p className="text-sm text-muted-foreground">
                  Общие параметры системы
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Analytics Tab */}
      {activeTab === 'analytics' && (
        <Card>
          <CardHeader>
            <CardTitle>Аналитика администратора</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-4 md:grid-cols-3">
              <div className="p-4 border rounded-lg">
                <div className="text-3xl font-bold">{users.length}</div>
                <div className="text-sm text-muted-foreground">Пользователей</div>
              </div>
              <div className="p-4 border rounded-lg">
                <div className="text-3xl font-bold">{roles.length}</div>
                <div className="text-sm text-muted-foreground">Ролей</div>
              </div>
              <div className="p-4 border rounded-lg">
                <div className="text-3xl font-bold">0</div>
                <div className="text-sm text-muted-foreground">Системных ошибок</div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Pagination */}
      {totalPages > 1 && activeTab === 'users' && (
        <div className="flex items-center justify-between mt-4">
          <div className="text-sm text-muted-foreground">
            Страница {userPage + 1} из {totalPages}
          </div>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setUserPage(Math.max(0, userPage - 1))}
              disabled={userPage === 0}
            >
              Назад
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setUserPage(Math.min(totalPages - 1, userPage + 1))}
              disabled={userPage === totalPages - 1}
            >
              Вперед
            </Button>
          </div>
        </div>
      )}

      {totalRoles > 1 && activeTab === 'roles' && (
        <div className="flex items-center justify-between mt-4">
          <div className="text-sm text-muted-foreground">
            Страница {rolePage + 1} из {totalRoles}
          </div>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setRolePage(Math.max(0, rolePage - 1))}
              disabled={rolePage === 0}
            >
              Назад
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setRolePage(Math.min(totalRoles - 1, rolePage + 1))}
              disabled={rolePage === totalRoles - 1}
            >
              Вперед
            </Button>
          </div>
        </div>
      )}

      {/* Create User Dialog */}
      <Dialog open={isUserDialogOpen} onOpenChange={setIsUserDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Новый пользователь</DialogTitle>
            <DialogDescription>
              Создайте нового пользователя системы
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="username">Имя пользователя</Label>
              <Input
                id="username"
                value={newUser.username}
                onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
                placeholder="Введите имя пользователя"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                value={newUser.email}
                onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                placeholder="Введите email"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="firstName">Имя</Label>
              <Input
                id="firstName"
                value={newUser.firstName}
                onChange={(e) => setNewUser({ ...newUser, firstName: e.target.value })}
                placeholder="Введите имя"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="lastName">Фамилия</Label>
              <Input
                id="lastName"
                value={newUser.lastName}
                onChange={(e) => setNewUser({ ...newUser, lastName: e.target.value })}
                placeholder="Введите фамилию"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Пароль</Label>
              <Input
                id="password"
                type="password"
                value={newUser.password}
                onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                placeholder="Введите пароль"
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsUserDialogOpen(false)}>
              Отмена
            </Button>
            <Button onClick={handleCreateUser} disabled={createUserMutation.isPending}>
              {createUserMutation.isPending ? 'Создание...' : 'Создать'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Create Role Dialog */}
      <Dialog open={isRoleDialogOpen} onOpenChange={setIsRoleDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Новая роль</DialogTitle>
            <DialogDescription>
              Создайте новую роль в системе
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="roleName">Название</Label>
              <Input
                id="roleName"
                value={newRole.name}
                onChange={(e) => setNewRole({ ...newRole, name: e.target.value })}
                placeholder="Введите название роли"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="roleCode">Код</Label>
              <Input
                id="roleCode"
                value={newRole.code}
                onChange={(e) => setNewRole({ ...newRole, code: e.target.value })}
                placeholder="Введите код роли"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="roleDescription">Описание</Label>
              <Textarea
                id="roleDescription"
                value={newRole.description}
                onChange={(e) => setNewRole({ ...newRole, description: e.target.value })}
                placeholder="Описание роли"
                rows={3}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsRoleDialogOpen(false)}>
              Отмена
            </Button>
            <Button onClick={handleCreateRole} disabled={createRoleMutation.isPending}>
              {createRoleMutation.isPending ? 'Создание...' : 'Создать'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}

export default AdminPage
