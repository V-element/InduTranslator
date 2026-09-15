import { api } from './api'

export interface ImportRecordDto {
  id: number
  sourceSystem: string
  sourceId: string
  type: string
  status: 'pending' | 'processing' | 'completed' | 'failed' | 'partial'
  totalRecords: number
  processedRecords: number
  failedRecords: number
  successRate: number
  errorMessage?: string
  errors?: ImportErrorDto[]
  importedAt?: string
  startedAt?: string
  completedAt?: string
  userId?: number
  username?: string
  createdAt?: string
}

export interface ImportErrorDto {
  id: number
  recordIndex: number
  fieldName?: string
  errorType: string
  message: string
  value?: string
}

export interface ImportResponse {
  content: ImportRecordDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface ImportRequest {
  sourceSystem: string
  type: string
  file?: File
  data?: any
  configuration?: Record<string, any>
}

export const ImportAPI = {
  getImports: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    statusFilter?: string,
    typeFilter?: string
  ): Promise<ImportResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (statusFilter && statusFilter !== 'all') params.append('status', statusFilter)
    if (typeFilter && typeFilter !== 'all') params.append('type', typeFilter)
    
    const response = await api.get<ImportResponse>(`/import?${params.toString()}`)
    return response.data
  },

  getImportById: async (id: number): Promise<ImportRecordDto> => {
    const response = await api.get<ImportRecordDto>(`/import/${id}`)
    return response.data
  },

  importData: async (request: ImportRequest): Promise<ImportRecordDto> => {
    const formData = new FormData()
    formData.append('sourceSystem', request.sourceSystem)
    formData.append('type', request.type)
    if (request.file) formData.append('file', request.file)
    if (request.data) formData.append('data', JSON.stringify(request.data))
    if (request.configuration) formData.append('configuration', JSON.stringify(request.configuration))
    
    const response = await api.post<ImportRecordDto>('/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },

  retryImport: async (id: number): Promise<ImportRecordDto> => {
    const response = await api.post<ImportRecordDto>(`/import/${id}/retry`)
    return response.data
  },

  cancelImport: async (id: number): Promise<ImportRecordDto> => {
    const response = await api.post<ImportRecordDto>(`/import/${id}/cancel`)
    return response.data
  },

  getImportStatistics: async (): Promise<any> => {
    const response = await api.get('/import/statistics')
    return response.data
  },

  getImportErrorDetails: async (importId: number, errorId: number): Promise<ImportErrorDto> => {
    const response = await api.get<ImportErrorDto>(`/import/${importId}/errors/${errorId}`)
    return response.data
  }
}
