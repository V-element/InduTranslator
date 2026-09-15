import { api } from './api'

export interface IntegrationDto {
  id: number
  name: string
  code: string
  description?: string
  type: 'api' | 'database' | 'file' | 'custom'
  apiUrl?: string
  apiKey?: string
  config?: Record<string, any>
  mapping?: Record<string, string>
  active: boolean
  lastSyncAt?: string
  lastSyncDuration?: number
  lastStatus?: 'success' | 'error' | 'in-progress' | 'never'
  lastErrorMessage?: string
  syncFrequency?: number
  syncSchedule?: string
  createdAt?: string
  updatedAt?: string
}

export interface IntegrationResponse {
  content: IntegrationDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface SyncRequest {
  integrationId: number
  manual?: boolean
  entities?: string[]
}

export const IntegrationAPI = {
  getAllIntegrations: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    typeFilter?: string,
    activeFilter?: boolean
  ): Promise<IntegrationResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (typeFilter && typeFilter !== 'all') params.append('type', typeFilter)
    if (activeFilter !== undefined) params.append('active', activeFilter.toString())
    
    const response = await api.get<IntegrationResponse>(`/integrations?${params.toString()}`)
    return response.data
  },

  getIntegrationById: async (id: number): Promise<IntegrationDto> => {
    const response = await api.get<IntegrationDto>(`/integrations/${id}`)
    return response.data
  },

  testConnection: async (id: number): Promise<any> => {
    const response = await api.post(`/integrations/${id}/test`)
    return response.data
  },

  syncNow: async (id: number, entities?: string[]): Promise<any> => {
    const response = await api.post(`/integrations/${id}/sync`, { entities })
    return response.data
  },

  createIntegration: async (integration: Partial<IntegrationDto>): Promise<IntegrationDto> => {
    const response = await api.post<IntegrationDto>('/integrations', integration)
    return response.data
  },

  updateIntegration: async (id: number, integration: Partial<IntegrationDto>): Promise<IntegrationDto> => {
    const response = await api.put<IntegrationDto>(`/integrations/${id}`, integration)
    return response.data
  },

  deleteIntegration: async (id: number): Promise<void> => {
    await api.delete(`/integrations/${id}`)
  },

  activateIntegration: async (id: number): Promise<IntegrationDto> => {
    const response = await api.patch<IntegrationDto>(`/integrations/${id}/activate`)
    return response.data
  },

  deactivateIntegration: async (id: number): Promise<IntegrationDto> => {
    const response = await api.patch<IntegrationDto>(`/integrations/${id}/deactivate`)
    return response.data
  },

  getIntegrationSyncHistory: async (id: number, page: number = 0, size: number = 10): Promise<any> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    const response = await api.get(`/integrations/${id}/sync-history?${params.toString()}`)
    return response.data
  }
}
