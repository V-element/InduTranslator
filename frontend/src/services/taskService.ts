import { api } from './api'

export interface TaskDto {
  id: number
  title: string
  description?: string
  status: string
  priority: number
  departmentId?: number
  departmentName?: string
  createdAt?: string
  updatedAt?: string
}

export interface TaskResponse {
  content: TaskDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export const TaskAPI = {
  getAllTasks: async (
    page: number = 0,
    size: number = 10,
    sort: string = 'createdAt',
    filter?: string,
    statusFilter?: string,
    departmentFilter?: string
  ): Promise<TaskResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    params.append('sort', sort)
    
    if (filter) params.append('filter', filter)
    if (statusFilter && statusFilter !== 'all') params.append('status', statusFilter)
    if (departmentFilter && departmentFilter !== 'all') params.append('departmentId', departmentFilter)
    
    const response = await api.get<TaskResponse>(`/tasks?${params.toString()}`)
    return response.data
  },

  getTaskById: async (id: number): Promise<TaskDto> => {
    const response = await api.get<TaskDto>(`/tasks/${id}`)
    return response.data
  },

  searchTasks: async (
    query: string,
    page: number = 0,
    size: number = 10
  ): Promise<TaskResponse> => {
    const params = new URLSearchParams()
    params.append('query', query)
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    const response = await api.get<TaskResponse>(`/tasks/search?${params.toString()}`)
    return response.data
  },

  getTasksByStatus: async (status: string): Promise<TaskDto[]> => {
    const response = await api.get<TaskDto[]>(`/tasks/status/${status}`)
    return response.data
  },

  getTasksByDepartment: async (departmentId: number): Promise<TaskDto[]> => {
    const response = await api.get<TaskDto[]>(`/tasks/department/${departmentId}`)
    return response.data
  },

  getTaskStats: async (): Promise<any> => {
    const response = await api.get('/tasks/stats')
    return response.data
  },

  createTask: async (taskData: any): Promise<any> => {
    const response = await api.post('/tasks', taskData)
    return response.data
  },

  importTasks: async (file: File): Promise<number> => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await api.post('/tasks/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  exportTasks: async (): Promise<Blob> => {
    const response = await api.get('/tasks/export', {
      responseType: 'blob'
    })
    return response.data
  }
}
