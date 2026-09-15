import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { CheckCircle2, AlertTriangle, TrendingUp, ChevronDown, ChevronUp, ChevronLeft } from 'lucide-react'

// Mock data for Task Route
interface RouteStep {
  id: number
  name: string
  description?: string
  status: 'pending' | 'in-progress' | 'completed' | 'blocked'
  departmentName?: string
  assignedTo?: {
    username: string
    firstName?: string
    lastName?: string
  }
  slaHours?: number
  slaStartTime?: string
  slaEndTime?: string
  slaStatus?: 'on-track' | 'at-risk' | 'expired' | 'pending'
  dependencies?: string[]
  orderNumber: number
}

interface TaskRoute {
  id: number
  name: string
  description?: string
  status: string
  steps: RouteStep[]
}

const TaskRoutePage: React.FC = () => {
  const navigate = useNavigate()
  const [expandedSteps, setExpandedSteps] = useState<number[]>([])

  const toggleStep = (id: number) => {
    setExpandedSteps(prev => 
      prev.includes(id) ? prev.filter(sid => sid !== id) : [...prev, id]
    )
  }

  const routeData: TaskRoute = {
    id: 1,
    name: 'Процесс обработки заявки',
    description: 'Стандартный маршрут для обработки входящих заявок',
    status: 'in-progress',
    steps: [
      {
        id: 1,
        name: 'Регистрация заявки',
        description: 'Регистрация заявки в системе и присвоение номера',
        status: 'completed',
        departmentName: 'Входящая почта',
        assignedTo: { username: 'user1', firstName: 'Иван', lastName: 'Иванов' },
        slaHours: 24,
        slaStatus: 'on-track',
        dependencies: [],
        orderNumber: 1
      },
      {
        id: 2,
        name: 'Проверка документов',
        description: 'Проверка полноты и корректности предоставленных документов',
        status: 'completed',
        departmentName: 'Юридический отдел',
        assignedTo: { username: 'user2', firstName: 'Мария', lastName: 'Петрова' },
        slaHours: 48,
        slaStatus: 'on-track',
        dependencies: ['1'],
        orderNumber: 2
      },
      {
        id: 3,
        name: 'Анализ и адаптация',
        description: 'Анализ содержания и адаптация под требования отдела',
        status: 'in-progress',
        departmentName: 'Аналитический отдел',
        assignedTo: { username: 'user3', firstName: 'Алексей', lastName: 'Сидоров' },
        slaHours: 72,
        slaStatus: 'at-risk',
        dependencies: ['2'],
        orderNumber: 3
      },
      {
        id: 4,
        name: 'Финальная проверка',
        description: 'Проверка готового продукта перед отправкой',
        status: 'pending' as const,
        departmentName: 'Контроль качества',
        assignedTo: { username: 'user4', firstName: 'Елена', lastName: 'Козлова' },
        slaHours: 24,
        slaStatus: 'pending',
        dependencies: ['3'],
        orderNumber: 4
      },
      {
        id: 5,
        name: 'Отправка результатов',
        description: 'Отправка готового продукта заказчику',
        status: 'pending' as const,
        departmentName: 'Отдел обслуживания',
        assignedTo: { username: 'user5', firstName: 'Дмитрий', lastName: 'Васильев' },
        slaHours: 24,
        slaStatus: 'pending',
        dependencies: ['4'],
        orderNumber: 5
      }
    ]
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'completed': return 'bg-green-100 text-green-800'
      case 'in-progress': return 'bg-blue-100 text-blue-800'
      case 'pending': return 'bg-gray-100 text-gray-800'
      case 'blocked': return 'bg-red-100 text-red-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  const getSLAColor = (status?: string) => {
    switch (status) {
      case 'on-track': return 'text-green-600'
      case 'at-risk': return 'text-yellow-600'
      case 'expired': return 'text-red-600'
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
        <h1 className="text-3xl font-bold tracking-tight">Маршрут обработки</h1>
        <p className="text-muted-foreground">
          Визуализация потока выполнения задачи
        </p>
      </div>

      {/* Route Header */}
      <Card>
        <CardHeader>
          <div className="flex justify-between items-center">
            <div>
              <CardTitle className="text-2xl">{routeData.name}</CardTitle>
              <p className="text-muted-foreground mt-1">{routeData.description}</p>
            </div>
            <Badge className={getStatusColor(routeData.status)}>
              {routeData.status === 'completed' ? 'Завершен' : 
               routeData.status === 'in-progress' ? 'В работе' : 'Ожидание'}
            </Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4 mt-4">
            <div className="flex items-center gap-2">
              <CheckCircle2 className="h-5 w-5 text-green-600" />
              <span className="text-sm font-medium">
                {routeData.steps.filter(s => s.status === 'completed').length} / {routeData.steps.length} этапов
              </span>
            </div>
            <div className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5 text-red-600" />
              <span className="text-sm font-medium">
                {routeData.steps.filter(s => s.slaStatus === 'expired').length} просрочено
              </span>
            </div>
            <div className="flex items-center gap-2">
              <TrendingUp className="h-5 w-5 text-blue-600" />
              <span className="text-sm font-medium">85% SLA соблюдено</span>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Stepper */}
      <Card>
        <CardHeader>
          <CardTitle>Этапы выполнения</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="relative">
            {/* Vertical line */}
            <div className="absolute left-6 top-0 bottom-0 w-0.5 bg-gray-200" />

            <div className="space-y-6 pl-12">
              {routeData.steps.map((step) => (
                <div key={step.id} className="relative">
                  {/* Step indicator */}
                  <div className="absolute left-0 top-0 w-12 h-12 rounded-full bg-white border-4 border-gray-200 flex items-center justify-center z-10 shadow-sm">
                    <div className={`w-3 h-3 rounded-full ${
                      step.status === 'completed' ? 'bg-green-500' :
                      step.status === 'in-progress' ? 'bg-blue-500' :
                      step.status === 'pending' ? 'bg-gray-300' :
                      'bg-red-500'
                    }`} />
                  </div>

                  {/* Step content */}
                  <div className="p-4 border rounded-lg hover:shadow-md transition-shadow">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <h3 className="font-medium text-lg">{step.name}</h3>
                          <Badge className={getStatusColor(step.status)}>
                            {step.status === 'completed' ? 'Выполнено' :
                             step.status === 'in-progress' ? 'В работе' :
                             step.status === 'pending' ? 'Ожидание' : 'Заблокировано'}
                          </Badge>
                        </div>

                        {step.description && (
                          <p className="text-sm text-muted-foreground mb-3">
                            {step.description}
                          </p>
                        )}

                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                          {step.departmentName && (
                            <div>
                              <span className="text-muted-foreground text-xs block">Отдел</span>
                              {step.departmentName}
                            </div>
                          )}
                          {step.assignedTo && (
                            <div>
                              <span className="text-muted-foreground text-xs block">Ответственный</span>
                              {step.assignedTo.firstName} {step.assignedTo.lastName}
                            </div>
                          )}
                          {step.slaHours && (
                            <div>
                              <span className={`text-muted-foreground text-xs block`}>SLA</span>
                              <span className={getSLAColor(step.slaStatus)}>
                                {step.slaStatus === 'expired' ? 'Просрочено' :
                                 step.slaStatus === 'at-risk' ? 'Под угрозой' : 'В срок'}
                              </span>
                            </div>
                          )}
                        </div>

                        {/* Dependencies */}
                        {step.dependencies && step.dependencies.length > 0 && (
                          <div className="mt-3">
                            <span className="text-muted-foreground text-xs block mb-1">
                              Зависит от этапов: {step.dependencies.join(', ')}
                            </span>
                          </div>
                        )}

                        {/* Expandable details */}
                        {expandedSteps.includes(step.id) && (
                          <div className="mt-3 pt-3 border-t">
                            <div className="text-sm text-muted-foreground">
                              <p className="mb-1"><strong>ID:</strong> {step.id}</p>
                              <p><strong>Порядок:</strong> {step.orderNumber}</p>
                            </div>
                          </div>
                        )}

                        <Button 
                          variant="ghost" 
                          size="sm" 
                          className="mt-2"
                          onClick={() => toggleStep(step.id)}
                        >
                          {expandedSteps.includes(step.id) ? (
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
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>

      {/* SLA Summary */}
      <Card>
        <CardHeader>
          <CardTitle>SLA статистика</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {routeData.steps.map((step) => (
              <div key={step.id} className="flex items-center gap-4">
                <div className="w-32 text-sm font-medium">{step.name}</div>
                <div className="flex-1">
                  <div className="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                    <div 
                      className={`h-full ${
                        step.slaStatus === 'on-track' ? 'bg-green-500' :
                        step.slaStatus === 'at-risk' ? 'bg-yellow-500' : 'bg-red-500'
                      }`}
                      style={{ width: `${(step.slaHours || 0) > 0 ? 75 : 0}%` }}
                    />
                  </div>
                </div>
                <div className="w-24 text-right text-sm">
                  <span className={getSLAColor(step.slaStatus)}>
                    {step.slaStatus === 'expired' ? 'Просрочено' :
                     step.slaStatus === 'at-risk' ? 'Под угрозой' : 'В срок'}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default TaskRoutePage
