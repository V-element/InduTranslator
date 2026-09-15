import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { FileText, Upload, Trash2, Download, ChevronLeft } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { DocumentResponse, documentAPI } from '@/services/documentService'
import { Alert, AlertDescription } from '@/components/ui/alert'

const DocumentsPage: React.FC = () => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [searchTerm, setSearchTerm] = useState('')
  const {
    data: documents = [],
    isLoading,
    error
  } = useQuery({
    queryKey: ['documents'],
    queryFn: documentAPI.getAll,
  })
  
  const uploadMutation = useMutation({
    mutationFn: documentAPI.upload,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['documents'] })
    }
  })
  
  const deleteMutation = useMutation({
    mutationFn: documentAPI.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['documents'] })
    }
  })

  const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (!file) return

    try {
      await uploadMutation.mutateAsync({
        file,
        title: file.name,
        description: 'Uploaded document',
      })
    } catch (error) {
      console.error('Failed to upload document:', error)
    }
  }

  const handleDelete = async (id: number) => {
    if (window.confirm('Вы уверены, что хотите удалить этот документ?')) {
      try {
        await deleteMutation.mutateAsync(id)
      } catch (error) {
        console.error('Failed to delete document:', error)
      }
    }
  }

  const filteredDocuments = documents.filter(doc =>
    doc.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    doc.fileName?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div>
        <Button variant="outline" size="sm" onClick={() => navigate(-1)}>
          <ChevronLeft className="mr-2 h-4 w-4" />
          Назад
        </Button>
      </div>
      <div className="flex items-center justify-between flex-wrap gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Документы</h1>
          <p className="text-muted-foreground">
            Управление загруженными документами
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Input
            placeholder="Поиск документов..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-xs"
          />
          <label htmlFor="file-upload" className="cursor-pointer">
            <Button>
              <Upload className="mr-2 h-4 w-4" />
              Загрузить
            </Button>
          </label>
          <input
            id="file-upload"
            type="file"
            className="hidden"
            onChange={handleFileUpload}
            disabled={uploadMutation.isPending}
          />
          <Button variant="outline">
            <Download className="mr-2 h-4 w-4" />
            Экспорт
          </Button>
        </div>
      </div>

      {error && (
        <Alert variant="destructive">
          <AlertDescription>Ошибка загрузки документов: {(error as Error).message}</AlertDescription>
        </Alert>
      )}

      <Card>
        <CardHeader>
          <CardTitle>
            Все документы ({filteredDocuments.length})
          </CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="text-center py-8">Загрузка документов...</div>
          ) : filteredDocuments.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <FileText className="mx-auto h-12 w-12 mb-2 opacity-20" />
              <p>Нет документов. Загрузите ваш первый документ выше.</p>
            </div>
          ) : (
            <div className="space-y-4">
              {filteredDocuments.map((doc) => (
                <div
                  key={doc.id}
                  className="flex items-center justify-between p-4 border rounded-lg"
                >
                  <div className="flex items-center gap-4">
                    <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center">
                      <FileText className="h-6 w-6 text-primary" />
                    </div>
                    <div>
                      <div className="font-medium">{doc.title || doc.fileName || 'Без названия'}</div>
                      <div className="text-sm text-muted-foreground">
                        {doc.fileName} • {(doc.fileSize || 0) > 0 ? `${(doc.fileSize! / 1024).toFixed(2)} KB` : '-'}
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Button variant="outline" size="sm">
                      <Download className="h-4 w-4" />
                    </Button>
                    <Button 
                      variant="ghost" 
                      size="sm" 
                      className="text-red-500 hover:text-red-700"
                      onClick={() => handleDelete(doc.id)}
                      disabled={deleteMutation.isPending}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default DocumentsPage
