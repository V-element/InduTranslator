import { api } from './api'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'

export interface UploadDocumentRequest {
  file: File
  title: string
  description?: string
}

export interface DocumentResponse {
  id: number
  title: string
  description?: string
  contentType: string
  fileName: string
  fileSize?: number
  storagePath?: string
  bucketName?: string
  createdAt?: string
  updatedAt?: string
}

export const documentAPI = {
  upload: async (request: UploadDocumentRequest): Promise<DocumentResponse> => {
    const formData = new FormData()
    formData.append('file', request.file)
    formData.append('title', request.title)
    if (request.description) {
      formData.append('description', request.description)
    }
    
    const response = await api.post('/documents/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },

  getAll: async (): Promise<DocumentResponse[]> => {
    const response = await api.get('/documents')
    return response.data
  },

  getById: async (id: number): Promise<DocumentResponse> => {
    const response = await api.get(`/documents/${id}`)
    return response.data
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/documents/${id}`)
  },

  searchSimilar: async (embedding: string, limit?: number): Promise<DocumentResponse[]> => {
    const response = await api.get('/documents/search', {
      params: { embedding, limit },
    })
    return response.data
  },
}

export const useDocuments = () => {
  return useQuery({
    queryKey: ['documents'],
    queryFn: documentAPI.getAll,
  })
}

export const useUploadDocument = () => {
  const queryClient = useQueryClient()
  
  return useMutation({
    mutationFn: documentAPI.upload,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['documents'] })
    },
  })
}

export const useDeleteDocument = () => {
  const queryClient = useQueryClient()
  
  return useMutation({
    mutationFn: documentAPI.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['documents'] })
    },
  })
}
