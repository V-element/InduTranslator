import React, { useState } from 'react'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { FileText, CheckCircle2, AlertTriangle, TrendingUp, RefreshCw, ThumbsUp, ChevronDown, ChevronUp, Copy, ChevronLeft } from 'lucide-react'
import { AdaptationAPI } from '@/services/adaptationService'

const TaskAdaptationsPage: React.FC = () => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [activeTab, setActiveTab] = useState('all')
  const [expandedAdaptations, setExpandedAdaptations] = useState<number[]>([])
  const [copiedContent, setCopiedContent] = useState<number | null>(null)
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [statusFilter, setStatusFilter] = useState('all')
  const [fallbackFilter, setFallbackFilter] = useState('all')

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['adaptations', page, size, statusFilter, fallbackFilter],
    queryFn: () => {
      return AdaptationAPI.getAllAdaptations(
        page,
        size,
        undefined,
        statusFilter === 'all' ? undefined : statusFilter,
        undefined,
        fallbackFilter === 'all' ? undefined : fallbackFilter === 'true'
      )
    }
  })

  const adaptations = response?.content || []
  const totalPages = response?.totalPages || 0

  const toggleAdaptation = (id: number) => {
    setExpandedAdaptations(prev => 
      prev.includes(id) ? prev.filter(aid => aid !== id) : [...prev, id]
    )
  }

  const copyToClipboard = (text: string, id: number) => {
    navigator.clipboard.writeText(text)
    setCopiedContent(id)
    setTimeout(() => setCopiedContent(null), 2000)
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'approved': return 'bg-green-100 text-green-800'
      case 'pending': return 'bg-blue-100 text-blue-800'
      case 'rejected': return 'bg-red-100 text-red-800'
      case 'draft': return 'bg-gray-100 text-gray-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  const handleRegenerate = async (adaptationId: number) => {
    try {
      const result = await AdaptationAPI.regenerateAdaptation({ adaptationId })
      await queryClient.invalidateQueries({ queryKey: ['adaptations'] })
      alert('Адаптация успешно обновлена!')
      console.log('Regenerated adaptation:', result)
    } catch (error) {
      console.error('Failed to regenerate:', error)
      alert('Ошибка при обновлении адаптации')
    }
  }

  const handleApprove = async (adaptationId: number) => {
    try {
      const result = await AdaptationAPI.approveAdaptation(adaptationId)
      await queryClient.invalidateQueries({ queryKey: ['adaptations'] })
      alert('Адаптация успешно одобрена!')
      console.log('Approved adaptation:', result)
    } catch (error) {
      console.error('Failed to approve:', error)
      alert('Ошибка при одобрении адаптации')
    }
  }

  const handleReject = async (adaptationId: number) => {
    try {
      const reason = prompt('Введите причину отклонения:')
      if (reason) {
        const result = await AdaptationAPI.rejectAdaptation(adaptationId, reason)
        await queryClient.invalidateQueries({ queryKey: ['adaptations'] })
        alert('Адаптация отклонена')
        console.log('Rejected adaptation:', result)
      }
    } catch (error) {
      console.error('Failed to reject:', error)
      alert('Ошибка при отклонении адаптации')
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка адаптаций...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки адаптаций: {(error as Error).message}</AlertDescription>
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
        <h1 className="text-3xl font-bold tracking-tight">AI-адаптации</h1>
        <p className="text-muted-foreground">
          Просмотр и управление адаптациями
        </p>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Всего адаптаций</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{adaptations.length}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Одобрено</CardTitle>
            <CheckCircle2 className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {adaptations.filter(a => a.status === 'approved').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">В ожидании</CardTitle>
            <TrendingUp className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {adaptations.filter(a => a.status === 'pending').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Fallback</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {adaptations.filter(a => a.isFallback).length}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
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
                <option value="draft">Черновик</option>
                <option value="pending">В ожидании</option>
                <option value="approved">Одобрено</option>
                <option value="rejected">Отклонено</option>
              </select>
            </div>

            <div className="w-full md:w-48">
              <select
                value={fallbackFilter}
                onChange={(e) => setFallbackFilter(e.target.value)}
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
              >
                <option value="all">Все адаптации</option>
                <option value="true">Только Fallback</option>
                <option value="false">Без Fallback</option>
              </select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
        <TabsList className="grid grid-cols-4 mb-4">
          <TabsTrigger value="all">Все</TabsTrigger>
          <TabsTrigger value="approved">Одобрено</TabsTrigger>
          <TabsTrigger value="pending">В ожидании</TabsTrigger>
          <TabsTrigger value="fallback">Fallback</TabsTrigger>
        </TabsList>

        <TabsContent value={activeTab} className="space-y-4">
          {adaptations.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет адаптаций, соответствующих фильтрам</p>
            </div>
          ) : (
            adaptations.map((adaptation) => (
              <Card key={adaptation.id}>
                <CardHeader>
                  <div className="flex justify-between items-start">
                    <div>
                      <CardTitle className="text-lg">{adaptation.title || adaptation.id?.toString() || 'Без названия'}</CardTitle>
                      <div className="flex items-center gap-2 mt-2">
                        <Badge className={getStatusColor(adaptation.status)}>
                          {adaptation.status === 'approved' ? 'Одобрено' :
                           adaptation.status === 'pending' ? 'В ожидании' :
                           adaptation.status === 'rejected' ? 'Отклонено' : 'Черновик'}
                        </Badge>
                        {adaptation.isFallback && (
                          <Badge variant="outline" className="text-red-600 border-red-200">
                            Fallback
                          </Badge>
                        )}
                        <Badge variant="outline">
                          {adaptation.adaptationType}
                        </Badge>
                      </div>
                    </div>
                    <div className="flex gap-2">
                      {adaptation.status === 'pending' && (
                        <>
                          <Button variant="outline" size="sm" onClick={() => handleRegenerate(adaptation.id)}>
                            <RefreshCw className="h-4 w-4 mr-2" />
                            Regenerate
                          </Button>
                          <Button variant="outline" size="sm" onClick={() => handleApprove(adaptation.id)}>
                            <ThumbsUp className="h-4 w-4 mr-2" />
                            Approve
                          </Button>
                        </>
                      )}
                      {adaptation.status === 'pending' && (
                        <Button variant="outline" size="sm" onClick={() => handleReject(adaptation.id)}>
                          Отклонить
                        </Button>
                      )}
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {/* Compare View */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="border rounded-lg p-4">
                        <div className="text-sm font-medium mb-2">Исходный текст</div>
                        <p className="text-sm text-muted-foreground line-clamp-3">
                          {adaptation.originalContent || 'Нет данных'}
                        </p>
                      </div>
                      <div className="border rounded-lg p-4">
                        <div className="text-sm font-medium mb-2">Адаптированный текст</div>
                        <p className="text-sm text-foreground line-clamp-3">
                          {adaptation.adaptedContent || 'Нет данных'}
                        </p>
                        <Button 
                          variant="ghost" 
                          size="sm" 
                          className="mt-2 h-8 text-xs"
                          onClick={() => copyToClipboard(adaptation.adaptedContent, adaptation.id)}
                        >
                          {copiedContent === adaptation.id ? (
                            <CheckCircle2 className="h-3 w-3 mr-2" />
                          ) : (
                            <Copy className="h-3 w-3 mr-2" />
                          )}
                          {copiedContent === adaptation.id ? 'Скопировано' : 'Копировать'}
                        </Button>
                      </div>
                    </div>

                    {/* Details */}
                    {expandedAdaptations.includes(adaptation.id) && (
                      <div className="pt-4 border-t space-y-3 animate-in fade-in slide-in-from-top-2">
                        {adaptation.reason && (
                          <div>
                            <span className="text-xs text-muted-foreground block mb-1">Причина изменения</span>
                            <p className="text-sm">{adaptation.reason}</p>
                          </div>
                        )}
                        {adaptation.citations && adaptation.citations.length > 0 && (
                          <div>
                            <span className="text-xs text-muted-foreground block mb-1">Ссылки</span>
                            <div className="flex flex-wrap gap-2">
                              {adaptation.citations.map((citation, idx) => (
                                <Badge key={idx} variant="secondary">
                                  {citation}
                                </Badge>
                              ))}
                            </div>
                          </div>
                        )}
                        {(adaptation.departmentName || adaptation.createdAt) && (
                          <div className="text-xs text-muted-foreground flex flex-wrap gap-4">
                            {adaptation.departmentName && <span>Отдел: {adaptation.departmentName}</span>}
                            {adaptation.createdAt && <span>Создано: {new Date(adaptation.createdAt).toLocaleDateString()}</span>}
                          </div>
                        )}
                      </div>
                    )}

                    <Button 
                      variant="ghost" 
                      size="sm"
                      onClick={() => toggleAdaptation(adaptation.id)}
                    >
                      {expandedAdaptations.includes(adaptation.id) ? (
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
                </CardContent>
              </Card>
            ))
          )}
        </TabsContent>
      </Tabs>

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

export default TaskAdaptationsPage
