import { api } from './api'

export interface AdaptationDto {
  id: number
  taskId?: number
  taskTitle?: string
  originalContent: string
  adaptedContent: string
  adaptationType: string
  reason: string
  citations: string[]
  isFallback: boolean
  status: 'draft' | 'pending' | 'approved' | 'rejected'
  departmentId?: number
  departmentName?: string
  userId?: number
  username?: string
  createdAt?: string
  updatedAt?: string
  title?: string
}

export interface AdaptationResponse {
  content: AdaptationDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface RegenerateRequest {
  adaptationId: number
  prompt?: string
  temperature?: number
}

export const AdaptationAPI = {
  getAllAdaptations: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    statusFilter?: string,
    departmentFilter?: string,
    fallbackFilter?: boolean
  ): Promise<AdaptationResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (statusFilter && statusFilter !== 'all') params.append('status', statusFilter)
    if (departmentFilter && departmentFilter !== 'all') params.append('departmentId', departmentFilter)
    if (fallbackFilter !== undefined) params.append('isFallback', fallbackFilter.toString())
    
    const response = await api.get<AdaptationResponse>(`/adaptations?${params.toString()}`)
    return response.data
  },

  getAdaptationById: async (id: number): Promise<AdaptationDto> => {
    const response = await api.get<AdaptationDto>(`/adaptations/${id}`)
    return response.data
  },

  getAdaptationsByTask: async (taskId: number): Promise<AdaptationDto[]> => {
    const response = await api.get<AdaptationDto[]>(`/tasks/${taskId}/adaptations`)
    return response.data
  },

  regenerateAdaptation: async (request: RegenerateRequest): Promise<AdaptationDto> => {
    const response = await api.post<AdaptationDto>(`/adaptations/${request.adaptationId}/regenerate`, request)
    return response.data
  },

  approveAdaptation: async (id: number): Promise<AdaptationDto> => {
    const response = await api.patch<AdaptationDto>(`/adaptations/${id}/approve`)
    return response.data
  },

  rejectAdaptation: async (id: number, reason: string): Promise<AdaptationDto> => {
    const response = await api.patch<AdaptationDto>(`/adaptations/${id}/reject`, { reason })
    return response.data
  },

  createFallback: async (adaptationId: number): Promise<AdaptationDto> => {
    const response = await api.post<AdaptationDto>(`/adaptations/${adaptationId}/fallback`)
    return response.data
  },

  getAdaptationStats: async (): Promise<any> => {
    const response = await api.get('/adaptations/stats')
    return response.data
  }
}
