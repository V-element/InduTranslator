import { api } from './api'

export interface ExecutionDto {
  id: number
  stepId: number
  stepName: string
  taskId: number
  taskTitle?: string
  status: 'pending' | 'in-progress' | 'completed' | 'blocked' | 'cancelled'
  assignedToId?: number
  assignedToUsername?: string
  assignedToFullName?: string
  departmentId?: number
  departmentName?: string
  startDate?: string
  endDate?: string
  slaHours?: number
  slaStatus?: 'on-track' | 'at-risk' | 'expired'
  comments?: string[]
  attachments?: AttachmentDto[]
  acknowledgements?: AcknowledgementDto[]
  orderNumber: number
  createdAt?: string
  updatedAt?: string
}

export interface AttachmentDto {
  id: number
  title: string
  fileName: string
  fileSize?: number
  contentType: string
  storagePath?: string
  uploadedBy?: string
  uploadedAt?: string
}

export interface AcknowledgementDto {
  id: number
  userId: number
  username: string
  acknowledgedAt?: string
  comment?: string
}

export interface ExecutionResponse {
  content: ExecutionDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export const ExecutionAPI = {
  getAllExecutions: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    statusFilter?: string,
    departmentFilter?: string,
    taskFilter?: number
  ): Promise<ExecutionResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (statusFilter && statusFilter !== 'all') params.append('status', statusFilter)
    if (departmentFilter && departmentFilter !== 'all') params.append('departmentId', departmentFilter)
    if (taskFilter) params.append('taskId', taskFilter.toString())
    
    const response = await api.get<ExecutionResponse>(`/executions?${params.toString()}`)
    return response.data
  },

  getExecutionById: async (id: number): Promise<ExecutionDto> => {
    const response = await api.get<ExecutionDto>(`/executions/${id}`)
    return response.data
  },

  getExecutionsByTask: async (taskId: number): Promise<ExecutionDto[]> => {
    const response = await api.get<ExecutionDto[]>(`/tasks/${taskId}/executions`)
    return response.data
  },

  getExecutionsByStep: async (stepId: number): Promise<ExecutionDto[]> => {
    const response = await api.get<ExecutionDto[]>(`/steps/${stepId}/executions`)
    return response.data
  },

  startExecution: async (id: number): Promise<ExecutionDto> => {
    const response = await api.patch<ExecutionDto>(`/executions/${id}/start`)
    return response.data
  },

  completeExecution: async (id: number, comment?: string, attachments?: number[]): Promise<ExecutionDto> => {
    const response = await api.patch<ExecutionDto>(`/executions/${id}/complete`, { comment, attachments })
    return response.data
  },

  blockExecution: async (id: number, reason: string): Promise<ExecutionDto> => {
    const response = await api.patch<ExecutionDto>(`/executions/${id}/block`, { reason })
    return response.data
  },

  unblockExecution: async (id: number): Promise<ExecutionDto> => {
    const response = await api.patch<ExecutionDto>(`/executions/${id}/unblock`)
    return response.data
  },

  cancelExecution: async (id: number, reason: string): Promise<ExecutionDto> => {
    const response = await api.patch<ExecutionDto>(`/executions/${id}/cancel`, { reason })
    return response.data
  },

  addAcknowledgement: async (id: number, comment?: string): Promise<AcknowledgementDto> => {
    const response = await api.post<AcknowledgementDto>(`/executions/${id}/acknowledge`, { comment })
    return response.data
  },

  addAttachment: async (id: number, attachmentId: number): Promise<ExecutionDto> => {
    const response = await api.post<ExecutionDto>(`/executions/${id}/attachments/${attachmentId}`)
    return response.data
  },

  removeAttachment: async (id: number, attachmentId: number): Promise<ExecutionDto> => {
    const response = await api.delete<ExecutionDto>(`/executions/${id}/attachments/${attachmentId}`)
    return response.data
  },

  addComment: async (id: number, comment: string): Promise<ExecutionDto> => {
    const response = await api.post<ExecutionDto>(`/executions/${id}/comments`, { comment })
    return response.data
  },

  getExecutionStats: async (): Promise<any> => {
    const response = await api.get('/executions/stats')
    return response.data
  }
}
