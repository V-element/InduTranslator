import { api } from './api'

export interface AnalyticsDto {
  period: string
  totalTasks: number
  completedTasks: number
  pendingTasks: number
  overdueTasks: number
  slaComplianceRate: number
  averageCompletionTime: number
  tasksByStatus: Record<string, number>
  tasksByDepartment: Record<string, number>
  tasksByPriority: Record<string, number>
}

export interface AnalyticsResponse {
  content: AnalyticsDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface AnalyticsFilters {
  startDate?: string
  endDate?: string
  departmentId?: number
  sourceSystemId?: number
  taskTypeId?: number
}

export const AnalyticsAPI = {
  getDashboardAnalytics: async (): Promise<any> => {
    const response = await api.get('/analytics/dashboard')
    return response.data
  },

  getTaskAnalytics: async (filters: AnalyticsFilters): Promise<any> => {
    const params = new URLSearchParams()
    if (filters.startDate) params.append('startDate', filters.startDate)
    if (filters.endDate) params.append('endDate', filters.endDate)
    if (filters.departmentId) params.append('departmentId', filters.departmentId.toString())
    if (filters.sourceSystemId) params.append('sourceSystemId', filters.sourceSystemId.toString())
    if (filters.taskTypeId) params.append('taskTypeId', filters.taskTypeId.toString())
    
    const response = await api.get('/analytics/tasks' + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  },

  getDepartmentAnalytics: async (departmentId: number, filters: AnalyticsFilters): Promise<any> => {
    const params = new URLSearchParams()
    if (filters.startDate) params.append('startDate', filters.startDate)
    if (filters.endDate) params.append('endDate', filters.endDate)
    
    const response = await api.get(`/analytics/department/${departmentId}` + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  },

  getROIAnalytics: async (departmentId?: number): Promise<any> => {
    const params = new URLSearchParams()
    if (departmentId) params.append('departmentId', departmentId.toString())
    
    const response = await api.get('/analytics/roi' + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  },

  getQualityAnalytics: async (filters: AnalyticsFilters): Promise<any> => {
    const params = new URLSearchParams()
    if (filters.startDate) params.append('startDate', filters.startDate)
    if (filters.endDate) params.append('endDate', filters.endDate)
    
    const response = await api.get('/analytics/quality' + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  },

  getTimeSeriesAnalytics: async (filters: AnalyticsFilters): Promise<any> => {
    const params = new URLSearchParams()
    if (filters.startDate) params.append('startDate', filters.startDate)
    if (filters.endDate) params.append('endDate', filters.endDate)
    if (filters.departmentId) params.append('departmentId', filters.departmentId.toString())
    
    const response = await api.get('/analytics/timeseries' + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  },

  getDrilldownAnalytics: async (drilldownType: string, drilldownValue?: string, filters: AnalyticsFilters = {}): Promise<any> => {
    const params = new URLSearchParams()
    if (filters.startDate) params.append('startDate', filters.startDate)
    if (filters.endDate) params.append('endDate', filters.endDate)
    if (filters.departmentId) params.append('departmentId', filters.departmentId.toString())
    if (drilldownValue) params.append('value', drilldownValue)
    
    const response = await api.get(`/analytics/drilldown/${drilldownType}` + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  },

  getReturnReasonsAnalytics: async (filters: AnalyticsFilters): Promise<any> => {
    const params = new URLSearchParams()
    if (filters.startDate) params.append('startDate', filters.startDate)
    if (filters.endDate) params.append('endDate', filters.endDate)
    
    const response = await api.get('/analytics/returns' + (params.toString() ? '?' + params.toString() : ''))
    return response.data
  }
}
