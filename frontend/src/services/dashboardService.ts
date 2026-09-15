import { api } from './api'

export interface DepartmentStats {
  departmentId: number
  departmentName: string
  taskCount: number
  percentage: number
  slaCompliance: number
}

export interface RecentEvent {
  id: number
  title: string
  description: string
  timestamp: string
  type: string
  severity: string
}

export interface DashboardStats {
  totalTasks: number
  completedTasks: number
  pendingTasks: number
  overdueTasks: number
  slaComplianceRate: number
  averageCompletionTime: number
}

export interface DashboardAnalytics {
  stats: DashboardStats
  departmentStats: DepartmentStats[]
  recentEvents: RecentEvent[]
}

export const DashboardAnalytics = {
  getDashboardAnalytics: async (): Promise<DashboardAnalytics> => {
    const response = await api.get<DashboardAnalytics>('/analytics/dashboard')
    return response.data
  },

  getTaskAnalytics: async (startDate?: string, endDate?: string): Promise<DashboardAnalytics> => {
    const params = new URLSearchParams()
    if (startDate) params.append('startDate', startDate)
    if (endDate) params.append('endDate', endDate)
    
    const response = await api.get<DashboardAnalytics>(`/analytics/tasks?${params.toString()}`)
    return response.data
  },

  getDepartmentAnalytics: async (
    departmentId: number,
    startDate?: string,
    endDate?: string
  ): Promise<DashboardAnalytics> => {
    const params = new URLSearchParams()
    if (startDate) params.append('startDate', startDate)
    if (endDate) params.append('endDate', endDate)
    
    const response = await api.get<DashboardAnalytics>(
      `/analytics/department/${departmentId}?${params.toString()}`
    )
    return response.data
  },

  getROIAnalytics: async (departmentId?: number): Promise<any> => {
    const params = new URLSearchParams()
    if (departmentId) params.append('departmentId', departmentId.toString())
    
    const response = await api.get<any>(`/analytics/roi?${params.toString()}`)
    return response.data
  },

  getQualityAnalytics: async (startDate?: string, endDate?: string): Promise<any> => {
    const params = new URLSearchParams()
    if (startDate) params.append('startDate', startDate)
    if (endDate) params.append('endDate', endDate)
    
    const response = await api.get<any>(`/analytics/quality?${params.toString()}`)
    return response.data
  }
}
