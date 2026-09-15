import React, { useState } from 'react'
import { useQuery, useMutation, QueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { FileText, Upload, AlertTriangle, CheckCircle2, ChevronLeft, X } from 'lucide-react'
import { ImportAPI } from '@/services/importService'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog'
import { Progress } from '@/components/ui/progress'

const queryClient = new QueryClient()

const ImportPage: React.FC = () => {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [uploadProgress, setUploadProgress] = useState(0)
  const [isUploading, setIsUploading] = useState(false)
  const [uploadError, setUploadError] = useState<string | null>(null)

  const {
    data: response,
    isLoading,
    error
  } = useQuery({
    queryKey: ['imports', page, size, statusFilter],
    queryFn: () => {
      return ImportAPI.getImports(
        page,
        size,
        undefined,
        statusFilter === 'all' ? undefined : statusFilter
      )
    }
  })

  const imports = response?.content || []
  const totalPages = response?.totalPages || 0

  const [dragActive, setDragActive] = useState(false)

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)
    
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFile(e.dataTransfer.files[0])
    }
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      handleFile(e.target.files[0])
    }
  }

  const handleFile = (file: File) => {
    setIsUploading(true)
    setUploadProgress(0)
    setUploadError(null)

    const formData = new FormData()
    formData.append('file', file)
    formData.append('sourceSystem', 'manual')
    formData.append('type', file.name.split('.').pop()?.toUpperCase() || 'CSV')

    // Simulate upload progress
    const progressInterval = setInterval(() => {
      setUploadProgress((prev) => {
        if (prev >= 100) {
          clearInterval(progressInterval)
          return 100
        }
        return prev + 10
      })
    }, 100)

    ImportAPI.importData(formData as any)
      .then(() => {
        queryClient.invalidateQueries({ queryKey: ['imports'] })
        setUploadProgress(100)
        setIsUploading(false)
        setDragActive(false)
        // Reset file input
        const input = document.getElementById('file-upload') as HTMLInputElement
        if (input) input.value = ''
      })
      .catch((error) => {
        setUploadError(error.message || 'Ошибка при загрузке файла')
        setIsUploading(false)
        clearInterval(progressInterval)
      })
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p className="text-gray-600">Загрузка импортов...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertDescription>Ошибка загрузки импортов: {(error as Error).message}</AlertDescription>
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
        <h1 className="text-3xl font-bold tracking-tight">Импорт данных</h1>
        <p className="text-muted-foreground">
          Загрузка и управление импортом данных
        </p>
      </div>

      {/* Import Form */}
      <Card>
        <CardHeader>
          <CardTitle>Загрузить файл</CardTitle>
        </CardHeader>
        <CardContent>
          <div
            className={`border-2 border-dashed rounded-lg p-8 text-center ${
              dragActive ? 'border-blue-500 bg-blue-50' : 'border-gray-300'
            }`}
            onDragEnter={handleDrag}
            onDragLeave={handleDrag}
            onDragOver={handleDrag}
            onDrop={handleDrop}
          >
            <Upload className="mx-auto h-12 w-12 text-gray-400 mb-4" />
            <p className="text-sm text-gray-600 mb-4">
              Перетащите файл сюда или{' '}
              <label className="text-blue-600 cursor-pointer underline">
                выберите файл
              </label>
            </p>
            <input
              type="file"
              className="hidden"
              onChange={handleChange}
              id="file-upload"
            />
            <div className="text-xs text-muted-foreground">
              Поддерживаемые форматы: CSV, JSON, XLSX
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Statistics */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Всего импортов</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{imports.length}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Успешные</CardTitle>
            <CheckCircle2 className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {imports.filter(i => i.status === 'completed').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">С ошибками</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {imports.filter(i => i.status === 'failed').length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Средний успех</CardTitle>
            <CheckCircle2 className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {imports.length > 0 
                ? Math.round(
                    imports.reduce((acc, i) => acc + i.successRate, 0) / imports.length
                  ) + '%'
                : '0%'}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Imports List */}
      <Card>
        <CardHeader>
          <CardTitle>
            История импортов ({imports.length} шт.)
          </CardTitle>
        </CardHeader>
        <CardContent>
          {imports.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет импортов</p>
            </div>
          ) : (
            <div className="space-y-4">
              {imports.map((imp) => (
                <div key={imp.id} className="border rounded-lg p-4">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <h3 className="font-medium">{imp.sourceSystem}</h3>
                        <span className="text-xs text-muted-foreground">[{imp.type}]</span>
                        <span className={`text-xs px-2 py-1 rounded-full ${
                          imp.status === 'completed' ? 'bg-green-100 text-green-800' :
                          imp.status === 'failed' ? 'bg-red-100 text-red-800' :
                          imp.status === 'partial' ? 'bg-yellow-100 text-yellow-800' :
                          'bg-gray-100 text-gray-800'
                        }`}>
                          {imp.status}
                        </span>
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {imp.totalRecords} записей, {imp.processedRecords} обработано,
                        {imp.failedRecords} ошибок
                      </div>
                      {imp.errorMessage && (
                        <div className="text-red-600 text-sm mt-2">
                          Ошибка: {imp.errorMessage}
                        </div>
                      )}
                    </div>
                    <div className="flex gap-2">
                      {imp.status === 'failed' && (
                        <Button variant="outline" size="sm" onClick={() => ImportAPI.retryImport(imp.id)}>
                          Повторить
                        </Button>
                      )}
                      <Button variant="outline" size="sm">
                        Подробнее
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
      {/* Upload Progress Dialog */}
      <Dialog open={isUploading} onOpenChange={(open) => { if (!open) setIsUploading(false) }}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Загрузка файла</DialogTitle>
          </DialogHeader>
          <div className="py-4 space-y-4">
            {uploadError ? (
              <Alert variant="destructive">
                <AlertDescription>{uploadError}</AlertDescription>
              </Alert>
            ) : (
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Загрузка данных...</span>
                  <span className="font-medium">{uploadProgress}%</span>
                </div>
                <Progress value={uploadProgress} className="h-2" />
                <p className="text-xs text-muted-foreground text-center">
                  Пожалуйста, подождите
                </p>
              </div>
            )}
          </div>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setIsUploading(false)
                setUploadError(null)
              }}
              disabled={!uploadError}
            >
              Закрыть
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}

export default ImportPage
